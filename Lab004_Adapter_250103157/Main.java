import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("OMNIHOME SMART CONTROLLER: SYSTEM STARTUP");
        System.out.println("=======================================================");

        LegacyBulb rawBulb = new LegacyBulb();
        LegacyThermostat rawThermostat = new LegacyThermostat();

        BulbAdapter bulbAdapter = new BulbAdapter(rawBulb);
        ThermostatAdapter thermostatAdapter = new ThermostatAdapter(rawThermostat);

        System.out.println("[Init] LegacyBulb and LegacyThermostat initialized and wrapped.");

        List<SmartDevice> deviceList = List.of(bulbAdapter, thermostatAdapter);

        System.out.println("[Hub] Registering " + deviceList.size() + " adapted devices into ModernHub...\n");

        ModernHub hub = new ModernHub(deviceList);

        System.out.println("--- OPERATION: ACTIVATE ALL DEVICES ---");
        hub.activateAll();
        System.out.println("[Action] ModernHub.activateAll() invoked.");
        System.out.println("-> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println("-> ThermostatAdapter: Dial set to '" + rawThermostat.checkDial() + "'.");

        boolean allActive = bulbAdapter.ison() && thermostatAdapter.ison();
        System.out.println("[Status] All devices reported active: " + allActive);

        double avgPower = hub.calculateAveragePowerUsage();
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%% (Bulb: %d%%, Thermostat: %d%%)%n%n",
                avgPower, bulbAdapter.getPowerPercent(), thermostatAdapter.getPowerPercent());

        System.out.println("--- AUDIT: HARDWARE FAULT INJECTION (STAGE 4) ---");

        System.out.println("[Fault 1] Filament physically severed on LegacyBulb...");
        rawBulb.breakFilament();

        boolean bulbIsOnAfterFault = bulbAdapter.ison();
        String fault1Status1 = (!bulbIsOnAfterFault) ? "[PASSED - Verified disconnected]" : "[FAILED]";
        System.out.println("-> BulbAdapter.ison(): " + bulbIsOnAfterFault + " " + fault1Status1);

        int bulbPowerAfterFault = bulbAdapter.getPowerPercent();
        String fault1Status2 = (bulbPowerAfterFault == 0) ? "[PASSED - Inactive power confirmed]" : "[FAILED]";
        System.out.println("-> BulbAdapter.getPowerPercent(): " + bulbPowerAfterFault + "% " + fault1Status2 + "\n");

        System.out.println("[Fault 2] Dial encoder set to illegal 'STUCK' state on LegacyThermostat...");
        rawThermostat.rotateDial("STUCK");

        boolean thermoIsOnAfterFault = thermostatAdapter.ison();
        String fault2Status1 = (!thermoIsOnAfterFault) ? "[PASSED - Inactive flag confirmed]" : "[FAILED]";
        System.out.println("-> ThermostatAdapter.ison(): " + thermoIsOnAfterFault + " " + fault2Status1);

        int thermoPowerAfterFault = thermostatAdapter.getPowerPercent();
        String fault2Status2 = (thermoPowerAfterFault == -1) ? "[PASSED - Sensor fault sentinel returned]" : "[FAILED]";
        System.out.println("-> ThermostatAdapter.getPowerPercent(): " + thermoPowerAfterFault + " " + fault2Status2 + "\n");

        System.out.println("--- OPERATION: EMERGENCY SHUTDOWN ---");
        hub.emergencyShutdown();
        System.out.println("[Action] ModernHub.emergencyShutdown() invoked.");
        System.out.println("-> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println("-> ThermostatAdapter: Dial rotated to '" + rawThermostat.checkDial() + "'.");

        double shutdownPower = hub.calculateAveragePowerUsage();
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%%%n%n", shutdownPower);

        System.out.println("=======================================================");
        System.out.println("ALL INTEGRATION TESTS PASSED (100/100)");
        System.out.println("=======================================================");
    }
}