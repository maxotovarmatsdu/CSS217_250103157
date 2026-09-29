public class BulbAdapter implements SmartDevice {

    private final LegacyBulb bulb;
    private final int calibrationSeedK;

    public static final int DEFAULT_K = 4;

    public BulbAdapter(LegacyBulb bulb) {
        this(bulb, DEFAULT_K);
    }

    public BulbAdapter(LegacyBulb bulb, int k) {
        if (bulb == null) {
            throw new IllegalArgumentException("Adaptee LegacyBulb reference cannot be null.");
        }
        this.bulb = bulb;
        this.calibrationSeedK = k;
    }

    @Override
    public void turnon() {
        this.bulb.setBrightness(255);
    }

    public void turnOn() {
        this.turnon();
    }

    @Override
    public void turnoff() {
        this.bulb.setBrightness(0);
    }

    public void turnOff() {
        this.turnoff();
    }

    @Override
    public boolean ison() {
        if (!this.bulb.hasPower()) {
            return false;
        }
        return this.bulb.readBrightness() > 0;
    }

    public boolean isOn() {
        return this.ison();
    }

    @Override
    public int getPowerPercent() {
        if (!this.bulb.hasPower()) {
            return 0;
        }

        int rawBrightness = this.bulb.readBrightness();

        if (rawBrightness == 0) {
            return 0;
        }

        int rawPercent = (rawBrightness * 100) / 255;
        int calibratedPercent = rawPercent + this.calibrationSeedK;

        return Math.min(100, calibratedPercent);
    }
}