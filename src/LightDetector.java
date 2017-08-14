import com.pi4j.io.gpio.*;


public class LightDetector {
	final GpioController gpio = GpioFactory.getInstance();
	final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
	final GpioPinDigitalInput sensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
}
