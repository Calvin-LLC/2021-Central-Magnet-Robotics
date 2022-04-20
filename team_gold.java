package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Func;

@TeleOp(name = "Team Gold Robnot", group = "best_opmode")
public class GoldRobnot<motor> extends LinearOpMode {

    // modifications/power
    private static final double pivot_mod  = .85;
    private static final double wheel_mod  = .55;
    private static final double duck_mod   = .4;
    private static final double claw_mod = .50;

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    private DcMotor arm_raise   = null;
    private DcMotor duck_wheel  = null;
    private Servo   gripper     = null;

    boolean y_toggle = false;
    @Override   // we're over-riding their function to run our code instead of ftc's!
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
            front_left = hardwareMap.get(DcMotor.class, "front_left");    // gobuilda move robot
            front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);       // brakes when you stop instead of sliding around everywhere
        } catch (Exception e) {
            telemetry.addData(">", "Error Finding front_left, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            front_right = hardwareMap.get(DcMotor.class, "front_right");
            front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding front_right, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            back_left = hardwareMap.get(DcMotor.class, "back_left");
            back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_left, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            back_right = hardwareMap.get(DcMotor.class, "back_right");
            back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_right, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_raise = hardwareMap.get(DcMotor.class, "arm_raise");
            arm_raise.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_raise, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            duck_wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
            duck_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

        while (opModeIsActive()) {

            // trying to get the arm_raise motor to lift the arm up and down
            /*if (arm_raise  != null) {
                arm_raise.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                if (gamepad1.a) {
                    arm_raise.setTargetPosition(360);
                    arm_raise.setPower(.2);
                }

                arm_raise.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }*/

            // right joy stick up and down will move arm up and down
            // a is top,
            // b is bottom level
            // x is to put it in the middle level

            // gamepad controllers
            double twist  = Math.pow(gamepad1.left_trigger - gamepad1.right_trigger, 3) * pivot_mod;
            double y = Math.pow(gamepad1.left_stick_y, 3); // i did the math and it works properly
            double x = Math.pow(gamepad1.left_stick_x, 3);


            if (arm_raise != null) {
                arm_raise.setPower(-gamepad2.left_stick_y * claw_mod);
            }


            // all arm power
            if (gripper != null) {
                if (gamepad2.y) {
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
            }

            if (duck_wheel != null) {
                double duck = ((gamepad2.left_bumper ? 1 : 0) - (gamepad2.right_bumper ? 1 : 0)) * duck_mod;
                duck_wheel.setPower(duck);
            }


            // wheel movement, makes sure all of the motors exist, shouldn't slow anything down
            if (front_left != null && front_right != null && back_left != null && back_right != null) {
                front_right.setPower((-y - x + twist) * wheel_mod);
                front_left.setPower((y - x + twist) * wheel_mod);
                back_right.setPower((-y + x + twist) * wheel_mod);
                back_left.setPower((y + x + twist) * wheel_mod);
            }


            // only update telemetry
            if (num_of_errors == 0) {
                telemetry.addData("x_power: ", x);
                telemetry.addData("y_power: ", y);
                telemetry.addData("twist: ", twist);
                telemetry.update();
            }
        }
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
