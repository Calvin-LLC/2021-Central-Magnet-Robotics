package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/* FTC Documentation & resources: */

/* ctrl click on the links to go to them
* season info: https://www.firstinspires.org/robotics/ftc/game-and-season
* ftc documentation: https://ftctechnh.github.io/ftc_app/doc/javadoc/index.html
* simple setup guide: https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/android-studio-guide.pdf*/

// you can look in FtcRobotController->java->org.firstinspires.ftc.robotcontroller->external.samples for examples

@TeleOp(name = "beta test", group = "best_opmode")
public class beta_test extends LinearOpMode {


    /* Drive vars, final since we only assign it once! */
    private final DcMotor frontLeft    = (DcMotor) hardwareMap.get(Servo.class, "frontLeft");          // Front left wheel motor
    private final DcMotor frontRight   = (DcMotor) hardwareMap.get(Servo.class, "frontRight");         // Front right wheel motor
    private final DcMotor backLeft     = (DcMotor) hardwareMap.get(Servo.class, "backLeft");           // Back left wheel motor
    private final DcMotor backRight    = (DcMotor) hardwareMap.get(Servo.class, "backRight");          // Back right wheel motor
    private final DcMotor belt         = (DcMotor) hardwareMap.get(Servo.class, "belt");               // Belt motor


    @Override   // we're over-riding their function to run our code instead of ftc's!
    public void runOpMode() {
        float Pwr;                // The joystick Y input, used as the forward and backward power
        float PivotL;             // The left trigger input, used to spin CCW
        float PivotR;             // The right trigger input, used to spin CW
        float strafe;             // The joystick X input, used to strafe side to side
        float spin_power = 0.30f; // The spin power
        boolean dpad = false;     // A boolean used to indicate whether the dpad has been pressed used for the changing of spin power

        waitForStart();
        while (opModeIsActive()) {
            /* Since joystick input is constantly changing, we want to update it as fast as possible,
            * that's why we're running it in a loop! */
            PivotL  = gamepad1.left_trigger * Math.abs(gamepad1.left_trigger) * Math.abs(gamepad1.left_trigger) * 0.85f;
            PivotR  = gamepad1.right_trigger * Math.abs(gamepad1.right_trigger) * Math.abs(gamepad1.right_trigger) * 0.85f;
            Pwr     = -gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y) * Math.abs(gamepad1.left_stick_y);
            strafe  = gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) * Math.abs(gamepad1.left_stick_x);


            /* Set wheel power based on stick and trigger input */
            frontLeft.setPower(Pwr - strafe + PivotL - PivotR);
            frontRight.setPower(-Pwr - strafe + PivotL - PivotR);
            backLeft.setPower(Pwr + strafe + PivotL - PivotR);
            backRight.setPower(-Pwr + strafe + PivotL - PivotR);


            /* Set spin power using dpad */
            if (gamepad2.dpad_up) {
                if (!dpad) {
                    spin_power += 0.02f;
                    dpad = true;
                }
            } else if (gamepad2.dpad_down) {
                if (!dpad) {
                    spin_power -= 0.02f;
                    dpad = true;
                }
            } else dpad = false;

            if (gamepad2.a) belt.setPower(-2);      // Turn belt forward when A is pressed
            else if (gamepad2.x) belt.setPower(2);  // Turn belt backward when X is pressed
            else belt.setPower(0);                  // Stop belt when neither A or X is pressed

            /* Adding Telemetry Data (for debugging) */
            telemetry.addData("Spin Power", Math.round(spin_power * 100));
            telemetry.update();
        }
    }
}
