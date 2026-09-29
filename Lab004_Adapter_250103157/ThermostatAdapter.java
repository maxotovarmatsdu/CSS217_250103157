public class ThermostatAdapter implements SmartDevice {

    private final LegacyThermostat thermostat;

    public ThermostatAdapter(LegacyThermostat thermostat) {
        if (thermostat == null) {
            throw new IllegalArgumentException("Adaptee LegacyThermostat reference cannot be null.");
        }
        this.thermostat = thermostat;
    }

    @Override
    public void turnon() {
        String currentDial = this.thermostat.checkDial();

        if ("IDLE".equalsIgnoreCase(currentDial)) {
            this.thermostat.rotateDial("LOW");
        }
    }

    public void turnOn() {
        this.turnon();
    }

    @Override
    public void turnoff() {
        this.thermostat.rotateDial("IDLE");
    }

    public void turnOff() {
        this.turnoff();
    }

    @Override
    public boolean ison() {
        String dial = this.thermostat.checkDial();

        if (dial == null) {
            return false;
        }

        switch (dial.toUpperCase().trim()) {
            case "LOW":
            case "MEDIUM":
            case "MAX":
                return true;

            case "IDLE":
                return false;

            default:
                return false;
        }
    }

    public boolean isOn() {
        return this.ison();
    }

    @Override
    public int getPowerPercent() {
        String dial = this.thermostat.checkDial();

        if (dial == null) {
            return -1;
        }

        switch (dial.toUpperCase().trim()) {
            case "IDLE":
                return 0;

            case "LOW":
                return 33;

            case "MEDIUM":
                return 66;

            case "MAX":
                return 100;

            default:
                return -1;
        }
    }
}