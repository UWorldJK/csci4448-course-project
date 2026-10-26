# Ticket Management Project - CSCI4448

## Authors
Jacob Kurry and Chiranth

## Project Overview
A comprehensive ticket management system for tracking stories, bugs, and epics with support for Git branch management, status tracking, and REST API integration.

## Architecture & Design Patterns

### Core Domain Classes

#### GitBranch.java
```java
public class GitBranch {
    String name;
    String repository;
    Integer numberOfCommits;

    void make();
    void merge();
    void rebase();
}
```

#### Ticket.java (Abstract Base Class)
```java
public abstract class Ticket {
    String assignee;
    String assignedBy;
    String epicName;
    String featureType;
    String statementOfWork;
    OffsetDateTime dueDate;

    GitBranch branch;
    Integer pointValue;

    abstract void addTestResults();
    abstract void changeStatus();
}
```

#### Story.java
```java
public class Story extends Ticket {
    String featureDetails;

    void addTestResults();
    void changeStatus();
}
```

#### BugFix.java
```java
public class BugFix extends Ticket {
    String bugDescription;

    void addTestResults();
    void changeStatus();
}
```

#### Epic.java
```java
public class Epic extends Ticket {
    String epicGoals;
    String releaseName;

    List<Ticket> userStories;
    List<Ticket> bugFixes;

    void addTestResults();
    void changeStatus();
}
```

#### Board.java (Kanban Board)
```java
public class Board {
    String boardName;
    String projectName;

    List<Ticket> backlog;
    List<Ticket> inProgress;
    List<Ticket> inReview;
    List<Ticket> done;

    void addTicket(Ticket ticket);
    void moveTicket(Ticket ticket, String status);
    void removeTicket(Ticket ticket);
    List<Ticket> getTicketsByStatus(String status);
    void displayBoard();
}
```
Represents a Kanban board with columns for different ticket statuses. When tickets are updated, their position on the board changes automatically.

### Design Patterns

#### 1. Facade Pattern - TicketManagementFacade.java
```java
public class TicketManagementFacade {
    private TicketFactory factory;
    private Board board;
    private DatabaseConnection db;
    private List<TicketObserver> observers;

    public Ticket createAndAddTicket(String type, String title, String assignee);
    public void updateTicketStatus(Ticket ticket, String newStatus);
    public void notifyAllObservers(Ticket ticket);
    public List<Ticket> getTicketsByStatus(String status);
    public Board getBoard();
}
```
Provides a simplified, unified interface to the complex subsystem of Ticket creation, Board management, Database persistence, and Observer notifications. Controllers interact with the facade instead of directly orchestrating multiple components.

#### 2. Factory Pattern - TicketFactory.java
```java
public abstract class TicketFactory {
    abstract Ticket createStory();
    abstract Ticket createEpic();
    abstract Ticket createBugFix();
}
```
Creates different ticket types while decoupling the creation logic from client code.

#### 2. Singleton Pattern - DatabaseConnection.java
```java
public class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        // Initialize database connection
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void executeQuery(String query);
    public ResultSet fetchData(String query);
    public void closeConnection();
}
```
Ensures a single database connection instance throughout the application, managing all database interactions.

#### 3. Observer Pattern - TicketObserver.java
```java
public interface TicketObserver {
    void update(Ticket ticket);
}
```

**Concrete Observers:**

- **StatusChangeObserver.java** - Notifies stakeholders when ticket status changes
- **AssigneeChangeObserver.java** - Alerts when ticket assignment changes
- **DueDateObserver.java** - Sends reminders as due dates approach
- **TestResultsObserver.java** - Triggers notifications when test results are added

The `Ticket` class maintains a list of observers and notifies them when state changes occur.

### REST API Integration

#### TicketController.java
REST endpoints for managing tickets:
```
GET    /api/tickets           - Retrieve all tickets
GET    /api/tickets/{id}      - Get specific ticket
POST   /api/tickets           - Create new ticket
PUT    /api/tickets/{id}      - Update ticket
DELETE /api/tickets/{id}      - Delete ticket
POST   /api/tickets/{id}/status - Change ticket status
```

#### BoardController.java
REST endpoints for managing Kanban boards:
```
GET    /api/boards            - Retrieve all boards
GET    /api/boards/{id}       - Get specific board with all tickets
GET    /api/boards/{id}/backlog     - Get backlog tickets
GET    /api/boards/{id}/in-progress - Get in-progress tickets
GET    /api/boards/{id}/in-review   - Get in-review tickets
GET    /api/boards/{id}/done        - Get completed tickets
POST   /api/boards            - Create new board
PUT    /api/boards/{id}/tickets/{ticketId}/move - Move ticket to different status
```

#### Data Flow
1. **REST Request** → Controller (TicketController or BoardController) receives HTTP request
2. **Service Layer** → Business logic processes request
3. **Board/Ticket Management** → Board maintains ticket organization, Ticket manages individual data
4. **Database Layer** → DatabaseConnection (Singleton) persists changes
5. **Observer Notification** → Changes trigger registered observers (status changes, assignments, etc.)
6. **REST Response** → JSON response with updated board state or ticket details returned to client

#### Example: Updating a Ticket Status
```
PUT /api/tickets/{id}/status
Request: { "newStatus": "in-progress" }
→ TicketObserver notifies all listeners
→ Board.moveTicket() automatically updates ticket position
→ DatabaseConnection persists the change
→ GET /api/boards/{id} reflects the updated board layout
```

## Class Dependencies
- `TicketManagementFacade` - Centralized access point for all ticket and board operations
  - Depends on `TicketFactory` for creating tickets
  - Depends on `Board` for managing ticket organization
  - Depends on `DatabaseConnection` (Singleton) for persistence
  - Manages `TicketObserver` notifications
- `Ticket` depends on `GitBranch` for branch management
- `Board` maintains collections of `Ticket` objects organized by status
- `TicketController` and `BoardController` delegate to `TicketManagementFacade`
- `TicketObserver` implementations listen to `Ticket` state changes
