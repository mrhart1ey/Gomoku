package mrhart1ey.gomoku.game;

/**
 * Represents a position on a Gomoku board.
 */
public final class Position {

    public final int row;
    public final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + this.row;
        hash = 11 * hash + this.column;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final Position other = (Position) obj;
        
        return this.row == other.row && this.column == other.column;
    }
    
    
}
