package ticketsystem.board;

public interface IBoard {
    void moveTicket();
    void addTicket();
    void getAllTickets();
    void getTicketsByColumn();

    void addNewColumn();
    void removeColumnHard();
    void removeColumnSoft();
}
