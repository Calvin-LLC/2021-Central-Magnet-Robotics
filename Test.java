package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;


/**
 * This 2020-2021 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Freight Frenzy game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@TeleOp(name = "Concept: TensorFlow Object Detection Webcam", group = "best_opmode")

public class Test extends LinearOpMode {
    /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
     * the following 4 detectable objects
     *  0: Ball,
     *  1: Cube,
     *  2: Duck,
     *  3: Marker (duck location tape marker)
     *
     *  Two additional model assets are available which only contain a subset of the objects:
     *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
     *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
     */
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    // modifications/power
    private static final double pivot_mod  = .85;
    private static final double wheel_mod  = .8;
    private static final double duck_mod   = .28;
    private static final double claw_mod = .5;

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    private DcMotor arm_raise   = null;
    private DcMotor duck_wheel  = null;
    private Servo   gripper     = null;

    double twist = 0;
    double y     = 0;
    double x     = 0;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AUZ8Yq7/////AAABmfI5k3dXeUf2q4igdbBWXjVJaGSs5QylBpPNhJS2DafVw8ZRFiHydrLcPjxieUSe4TrZpYdhElZSb7Tyo6JaTrEKB6tpHqtoty2tL5Yz4o6gZUwOnH5A+bLV7jGEmhGpAcKSlOwn3iOGqIBSMwrfET7I8ZZobpJlMOZZCCGsu37pX36/Yc1efCQuQtpHcCV/sDrl7I4f5ndbkDfppafMNjxEh3WftDi09WkJo+RGSxkFmLL+UxZs1IvGz4+UC9LLt8Bu3r/1R+HRxuNwfjNwYEAJ20fgLCkJsGfsiYYFkhqWXerm3CYLBxyDHhqMNkw109paOKe35gHU+6PF2XSqNvj2wcX43h9u1B0eBeZMuybe";
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    double central_point = 250;
    @Override
    public void runOpMode() {

        // shows the current battery life in volts whenever you run opmode
        telemetry.addData("voltage", "%.1f volts", new Func<Double>() {
            @Override public Double value() {
                return getBatteryVoltage();
            }
        });

        telemetry.addData(">", "Waiting For Start");

        int num_of_errors = 0;
        /* catches any errors and tells us what's not connected properly, if something isn't connected properly */
        try {
            front_left = hardwareMap.get(DcMotor.class, "front_left"); // gobuilda move robot
        } catch (Exception e) {
            telemetry.addData(">", "Error Finding front_left, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            front_right = hardwareMap.get(DcMotor.class, "front_right");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding front_right, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            back_left = hardwareMap.get(DcMotor.class, "back_left");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_left, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            back_right = hardwareMap.get(DcMotor.class, "back_right");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_right, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_raise = hardwareMap.get(DcMotor.class, "arm_raise");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_raise, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            duck_wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding duck_wheel, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            gripper = hardwareMap.get(Servo.class, "gripper");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding gripper servo, is it setup correctly?");
            ++num_of_errors;
        }
        telemetry.update();

        waitForStart();

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2.5, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();



        while (opModeIsActive()) {
            boolean marker_found = false;
            ArrayList<Double> x_coord = new ArrayList<Double>();
            ArrayList<Double> y_coord = new ArrayList<Double>();

            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        x_coord.set(i,(double) (recognition.getTop() / 2));
                        y_coord.set(i, (double) (recognition.getLeft() / 2));

                        if (x_coord.get(i) > central_point) {
                            telemetry.addData("Object found at ", x_coord.get(i));
                            telemetry.addData("Twist to the right", null);
                        } else if (x_coord.get(i) < central_point) {
                            telemetry.addData("Object found at ", x_coord.get(i));
                            telemetry.addData("Twist to the left", null);
                        }

                        if (y_coord.get(i) > central_point) {
                            telemetry.addData("Object found at ", y_coord.get(i));
                            telemetry.addData("Go Down!", null);
                        } else if (y_coord.get(i) < central_point) {
                            telemetry.addData("Object found at ", y_coord.get(i));
                            telemetry.addData("Go Up!", null);
                        }

                        if (recognition.getLabel().equals("Marker")) {
                            marker_found = true;
                            telemetry.addData("Marker found! ", i);
                        }
                        i++;
                    }
                    telemetry.update();
                }
            }

            if (!marker_found) {
                twist = .1;
            } else {
                twist = 0;
            }



            front_right.setPower((-y - x + twist) * wheel_mod);
            front_left.setPower((y - x + twist) * wheel_mod);
            back_right.setPower((-y + x + twist) * wheel_mod);
            back_left.setPower((y + x + twist) * wheel_mod);
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    // Computes the current battery voltage, from ConceptTelemetry file in the examples
    double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }
}