package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "Team black robnot", group = "best_opmode")
public class beta_test<motor> extends LinearOpMode {

    // modifications/power
    private final double pivot_mod  = .85;
    private final double wheel_mod  = 1;
    private final double arm_mod    = .5;


    // moto declarations
    private DcMotor front_left  = null; // gobuilda move robot
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;


    // arm shananaginz
    private DcMotor arm_pivot   = null;
    private DcMotor arm_raise   = null;
    private DcMotor duck_wheel  = null;
    private Servo gripper       = null;

    boolean y_toggle = false;

    @Override   // we're over-riding their function to run our code instead of ftc's!
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {
            /* Since joystick input is constantly changing, we want to update it as fast as possible,
             * that's why we're running it in a loop! */
            // The left trigger input, used to spin CCW
            front_left  = hardwareMap.get(DcMotor.class, "front_left");
            front_right = hardwareMap.get(DcMotor.class, "front_right");
            back_left   = hardwareMap.get(DcMotor.class, "back_left");
            back_right  = hardwareMap.get(DcMotor.class, "back_right");


            //arm_stand = hardwareMap.get(DcMotor.class, "arm_stand");
            gripper     = hardwareMap.get(Servo.class, "gripper");
            duck_wheel  = hardwareMap.get(DcMotor.class, "duck_wheel");
            //arm_pivot   = hardwareMap.get(DcMotor.class, "arm_pivot");
            //arm_raise   = hardwareMap.get(DcMotor.class, "arm_raise");


            // gamepad controllers
            double twist  = (gamepad1.right_trigger - gamepad1.left_trigger) * pivot_mod;
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;

            // all arm power
            if(gamepad1.y) {
                if (!y_toggle) {
                    gripper.setPosition(-gripper.getPosition());
                    y_toggle = true;
                } else {
                    gripper.setPosition(1.0);
                }
            } else {
                y_toggle = false;
                gripper.setPosition(0);
            }

            if (gamepad1.a) {
                duck_wheel.setPower(1);
            } else {
                duck_wheel.setPower(0);
            }


            // wheel movement
            front_right.setPower((y + x + twist) * wheel_mod);
            front_left.setPower((y - x + twist) * wheel_mod);
            back_right.setPower((-y - x - twist) * wheel_mod);
            back_left.setPower((-y + x - twist) * wheel_mod);


            // all new dope telemetry
            telemetry.addData("x_power: ", x);
            telemetry.addData("y_power: ", y);
            telemetry.addData("twist: ", twist);
            telemetry.update();
        }
    }
}
