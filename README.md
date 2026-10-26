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

Provides a simplified, unified interface to the complex subsystem of Ticket creation, Board management, Filesystem persistence, and Observer notifications.
Users interfacing with our service interact with the facade instead of directly orchestrating multiple components.

#### 2. Factory Pattern - TicketFactory.java

```java
public class TicketFactory {
    Ticket createStory();
    Ticket createEpic();
    Ticket createBugFix();
}
```

Creates different ticket types while decoupling the creation logic from client code. This will be useful across testing scenarios and unit tests.

#### 2. Singleton Pattern - FileStorgeManager.java

```java
public class FileStorgeManager {
    private final String filePath;

    private FileStorgeManager() {
    }

    // SpringBoot required that we be thread safe.
    public static synchronized FileStorgeManager getInstance() {
        if (instance == null) {
            instance = new FileStorgeManager();
        }
        return instance;
    }

    // Still working on the exact implementation and naming of these methods.
    void createTicketFile();
    void updateFile();

}
```

Ensures a single file storage manager instance throughout the application, managing all filesystem interactions.

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

### Testing

We plan to use Gherkin and Cucumber to create BDD tests and also TDD using JUnit. It becomes slightly challenging to make sure that we achieve 100%
test coverage on our endpoints and ensure the JSON schema is being.
Deserialized correctly.

```gherkin
Feature: Details of Ticket Creation
  As a scrum master I want to be able to create Tickets
  I want to ensure the lifecycle of a ticket created by me and assigned to a Developer
  I want to update, retrieve and analyze tickets

  Scenario: Creating a BugFix ticket
    Creating a kanban board with the following
      | Backlog |
      | In-Progress |
      | Done |

    And a BugFix ticket in the Backlog
    And a developer John is assigned to the BufFix ticket

    When John finishes the BugFix ticket

    Then Done column contains BugFix ticket
    And BugFix status is set to Done
```

```java
@WebMvcTest(TicketController.class)
class TicketControllerTest {
    @Autowired MockMvc mvc;
    @MockitoBean TicketService service;   // @MockBean on Boot < 3.4

    @Test
    void postCreatesTicket() throws Exception {
        when(service.create(any())).thenReturn(new Ticket(1L, "Broken login", "HIGH"));

        mvc.perform(post("/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"title":"Broken login","priority":"HIGH"}
                    """))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getReturns404WhenMissing() throws Exception {
        when(service.findById(99L)).thenThrow(new TicketNotFoundException(99L));
        mvc.perform(get("/tickets/99")).andExpect(status().isNotFound());
    }
}
```

### REST API Integration

#### TicketController.java

REST endpoints for managing tickets implemented with SpringBoot:

```
GET    /api/tickets           - Retrieve all tickets
GET    /api/tickets/{id}      - Get specific ticket
POST   /api/tickets           - Create new ticket
PUT    /api/tickets/{id}      - Update ticket
DELETE /api/tickets/{id}      - Delete ticket
POST   /api/tickets/{id}/status - Change ticket status
```

#### BoardController.java

REST endpoints for managing boards:

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
