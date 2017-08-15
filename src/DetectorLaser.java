package detectorlaser;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class DetectorLaser {

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
    final GpioController gpioLED = GpioFactory.getInstance();
    final GpioController gpioSensor = GpioFactory.getInstance();
    final GpioPinDigitalOutput led = gpioLED.provisionDigitalOutputPin(RaspiPin.GPIO_27);
    led.low();
    final GpioPinDigitalInput sensor = gpioSensor.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);
    sensor.setShutdownOptions(true);    
    //gpioLED.shutdown();
    //gpioSensor.shutdown();
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
        System.out.println("Led Off");
      }

      private void notifyLightsOff(final GpioPinDigitalOutput led) {
        led.high();
        System.out.println("Led On");
      }
    });
  }
      /*//private static void configureSenssor(final GpioPinDigitalOutput led, final GpioPinDigitalInput sensor) {
    System.out.println("hola1");
    sensor.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
        //handleSensorInput(led, event); 
      //}

      //private void handleSensorInput(final GpioPinDigitalOutput led, final GpioPinDigitalStateChangeEvent event) {
      
        if (event.getState().isLow()) {
          //notifyLightsOn(led);
          System.out.println("Led On");
          led.toggle();
        } 
        if (event.getState().isHigh()) {
          //notifyLightsOff(led);
          System.out.println("Led Off");
          led.low();
        }
      //}
      }
      private void notifyLightsOn(final GpioPinDigitalOutput led) {
        led.low();
        System.out.println("Led Off");
      }

      private void notifyLightsOff(final GpioPinDigitalOutput led) {
          led.high();
          System.out.println("Led On");
      }
    });
  //}
  
  sleep();*/
  
  private static void sleep() throws InterruptedException {
    for (;;) {
      Thread.sleep(500);  
    }
  }
}
