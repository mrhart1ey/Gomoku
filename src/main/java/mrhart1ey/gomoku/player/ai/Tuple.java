package mrhart1ey.gomoku.player.ai;

import java.util.Objects;

// A class to hold two values together
final class Tuple<V1, V2> {
    public final V1 value1;
    public final V2 value2;

    public Tuple(V1 value1, V2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.value1);
        hash = 79 * hash + Objects.hashCode(this.value2);
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
        
        final Tuple<?, ?> other = (Tuple<?, ?>) obj;
        
        return Objects.equals(this.value1, other.value1) && 
                Objects.equals(this.value2, other.value2);
    }
}
