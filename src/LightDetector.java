import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class LightDetector {

  private static final String LIGHTS_ON_MSG = "The lights are off";
  private static final String LIGHTS_OFF_MSG = "The lights are on";

  static LightStatus currentStatus;

  enum LightStatus {
    ON(LIGHTS_OFF_MSG), OFF(LIGHTS_ON_MSG);
    String status;

    private LightStatus(String status) {
      this.status = status;
    }
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Starting the laser detector...");
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
    final GpioPinDigitalInput sensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
    configureSenssor(led, sensor);
    sleep();
  }

  private static void configureSenssor(final GpioPinDigitalOutput led, final GpioPinDigitalInput sensor) {
    sensor.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
        handleSensorInput(led, event);
      }

      private void handleSensorInput(final GpioPinDigitalOutput led, final GpioPinDigitalStateChangeEvent event) {
        if (event.getState().isLow()) {
          notifyLightsOff(led);
        } else {
          notifyLightsOn(led);
        }
      }

      private void notifyLightsOn(final GpioPinDigitalOutput led) {
        led.low();
        System.out.println(LightStatus.OFF);
      }

      private void notifyLightsOff(final GpioPinDigitalOutput led) {
        led.high();
        System.out.println(LightStatus.OFF);
      }
    });
  }

  private static void sleep() throws InterruptedException {
    for (;;) {
      Thread.sleep(500);
    }
  }
}
