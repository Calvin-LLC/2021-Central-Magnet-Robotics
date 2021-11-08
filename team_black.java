package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/* FTC Documentation & resources: */

/* ctrl click on the links to go to them
 * season info: https://www.firstinspires.org/robotics/ftc/game-and-season
 * ftc documentation: https://ftctechnh.github.io/ftc_app/doc/javadoc/index.html
 * simple setup guide: https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/android-studio-guide.pdf*/

// you can look in FtcRobotController->java->org.firstinspires.ftc.robotcontroller->external.samples for examples

@TeleOp(name = "Black Robot v0.1", group = "best_opmode")
public class motor_test<motor> extends LinearOpMode {

    // modifications/power
    private final double duck_power = .278;
    private final double pivot_mod = .85;
    private final double wheel_mod = 1;
    private final double lazy_power = 1;
    private final double arm_out_power = 1;
    private final double arm_up_power = 1;


    // moto declarations
    private DcMotor front_left  = null; // gobuilda move robot
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    private DcMotor arm_up  = null; // torquenado, needs positive and negative
    private DcMotor arm_out = null;

    private DcMotor lazy = null; // corehex for lazysusan spin
    private DcMotor duck = null; // gobuilda motor, only one direction

    // servo
    private Servo claw = null;


    @Override   // we're over-riding their function to run our code instead of ftc's!
    public void runOpMode() {

        boolean y_toggle = false;
        waitForStart();
        while (opModeIsActive()) {
            /* Since joystick input is constantly changing, we want to update it as fast as possible,
             * that's why we're running it in a loop! */
            // The left trigger input, used to spin CCW
            front_left  = hardwareMap.get(DcMotor.class, "front_left");
            front_right = hardwareMap.get(DcMotor.class, "front_right");
            back_left   = hardwareMap.get(DcMotor.class, "back_left");
            back_right  = hardwareMap.get(DcMotor.class, "back_right");

            arm_up  = hardwareMap.get(DcMotor.class, "arm_up");
            arm_out = hardwareMap.get(DcMotor.class, "arm_out");
            lazy    = hardwareMap.get(DcMotor.class, "lazy");
            duck    = hardwareMap.get(DcMotor.class, "duck");

            claw  = hardwareMap.get(Servo.class, "claw");


            // gamepad controllers
            double left_pivot  = gamepad1.left_trigger * gamepad1.left_trigger * Math.abs(gamepad1.left_trigger) * pivot_mod;
            double right_pivot = gamepad1.right_trigger * gamepad1.right_trigger * Math.abs(gamepad1.right_trigger) * pivot_mod;
            double power       = (gamepad1.left_stick_y * gamepad1.left_stick_y * gamepad1.left_stick_y)*wheel_mod;
            double strafe      = gamepad1.left_stick_x * gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x);


            // set wheel power based on stick and trigger input
            front_left.setPower(power - strafe + left_pivot - right_pivot);
            front_right.setPower(-power - strafe + left_pivot - right_pivot);
            back_left.setPower(power + strafe + left_pivot - right_pivot);
            back_right.setPower(-power + strafe + left_pivot - right_pivot);

            // This makes the duck wheel spin
            if(gamepad1.a) {
                duck.setPower(duck_power);
            }

            // Y opens the claw, letting go closes it. If not being held, respond to right stick.
            if(gamepad1.y) {
                if (!y_toggle) {
                    claw.setPosition(-claw.getPosition());
                    y_toggle = true;
                } else {
                    claw.setPosition(1.0);
                }
            } else {
                y_toggle = false;
                claw.setPosition((Math.abs(gamepad1.right_stick_y))*(gamepad1.right_stick_y)*(gamepad1.right_stick_y));
            }

            // Extend the arm.
            if(gamepad1.x) {
                arm_out.setPower(arm_out_power);
            } else if(gamepad1.b) arm_out.setPower(-arm_out_power);

            // Move the arm the duck wheel is on
            if(gamepad1.right_bumper) {
                lazy.setPower(lazy_power);
            } else if(gamepad1.left_bumper) {
                lazy.setPower(-lazy_power);
            }

            // moves da arm
            if(gamepad1.dpad_up) {
                arm_up.setPower(arm_up_power);
            } else if (gamepad1.dpad_down) arm_up.setPower(-arm_up_power);
        }
    }
}
