package wordielv2.generator;

public class SpiralGenerator {

    public SpiralGenerator(Vector spiralElement) {
        this.spiralElement = spiralElement;
        reset();
    }
    private Vector spiralElement;
    private int count;

    public Vector nextSegment() {
        count++;
        return this.getCoordinateInSpiral(count);
    }

    public final void reset() {
        count = 0;
    }

    /**
     * Returns the next segment of the spiral.
     *
     * @param n
     * @return the next segment
     */
    public Vector getCoordinateInSpiral(int n) {
        int x = (int) spiralElement.getX(), z = (int) spiralElement.getY();
        if (n - 1 >= 0) {
            int v = (int) Math.floor(Math.sqrt(n + .25) - 0.5);
            int spiralBaseIndex = v * (v + 1);
            int flipFlop = ((v & 1) << 1) - 1;
            int offset = flipFlop * ((v + 1) >> 1);
            x += offset;
            z += offset;
            int cornerIndex = spiralBaseIndex + (v + 1);
            if (n < cornerIndex) {
                x -= flipFlop * (n - spiralBaseIndex + 1);
            } else {
                x -= flipFlop * (v + 1);
                z -= flipFlop * (n - cornerIndex + 1);
            }
        }
        return new Vector(x, z);
    }
}