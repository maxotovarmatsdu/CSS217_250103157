public class ThermostatAdapter implements SmartDevice {
    private final LegacyThermostat thermostat;

    public ThermostatAdapter(LegacyThermostat thermostat) {
        if (thermostat == null) {
            throw new IllegalArgumentException("Thermostat cannot be null");
        }
        this.thermostat = thermostat;
    }

    @Override
    public void turnOn() {
    }

    @Override
    public void turnOff() {
    }

    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public int getPowerPercent() {
        return 0;
    }
}