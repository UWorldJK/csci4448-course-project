package ticketsystem.board;

import ticketsystem.developer.Developer;
import ticketsystem.factory.TicketFactory;
import ticketsystem.tickets.Ticket;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Board implements IBoard {
    private final EnumMap<ColumnType, Column> columns;
    private final List<Developer> devs;
    private final String name;

    private Board(String name) {
        this.name = name;
        this.columns = new EnumMap<>(ColumnType.class);
        this.devs = new ArrayList<>();
    }

    public void moveTicket() {}
    public void addTicket() {}
    public void getAllTickets() {}
    public void getTicketsByColumn() {}

    public void addNewColumn() {}
    public void removeColumnHard() {}
    public void removeColumnSoft() {}

    public EnumMap<ColumnType, Column> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public List<Developer> getDevs() {
        return devs;
    }



    public static class Builder {
        private final Board board;
        private final TicketFactory ticketFactory;

        private Builder(String name, TicketFactory ticketFactory) {
            this.board = new Board(name);
            this.ticketFactory = ticketFactory;
        }

        public static Builder getNewBuilder(String name, TicketFactory ticketFactory) {
            return new Builder(name, ticketFactory);
        }

        public Builder createNColumns(List<ColumnType> columnTypes) {
            for (ColumnType type: columnTypes) {
                board.columns.put(type, new Column());
            }
            return this;
        }

        public Builder addTeamDevelopers(List<Developer> developers) {
            board.devs.addAll(developers);

            return this;
        }

        public Builder addTickets(Map<ColumnType, List<Ticket>> tickets) {
            for (Map.Entry<ColumnType, List<Ticket>> entry : tickets.entrySet()) {
                ColumnType columnType = entry.getKey();
                List<Ticket> ticketList = entry.getValue();

                Column column = board.columns.get(columnType);
                if (column != null) {
                    column.tickets.addAll(ticketList);
                }
            }
            return this;
        }

        public Board build() {
            return board;
        }
    }
}
