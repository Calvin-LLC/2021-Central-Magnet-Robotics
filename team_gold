package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;

@TeleOp(name = "Team gold robnot", group = "best_opmode")
public class gold_robnot<motor> extends LinearOpMode {

    // modifications/power
    private static final double pivot_mod  = .85;
    private static final double wheel_mod  = 1;
    private static final double duck_mod   = .28;

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
        telemetry.update();

        waitForStart();
        //ElapsedTime opmodeRunTime = new ElapsedTime();  // creates a timer, accessed by opmodeRunTime.seconds();

        /* catches any errors and tells us what's not connected properly, if something isn't connected properly */
        try {
            front_left = hardwareMap.get(DcMotor.class, "front_left"); // gobuilda move robot
        } catch (Exception e) {
            telemetry.addData(">", "Error Finding front_left, is it setup correctly?");
        }

        try {
            front_right = hardwareMap.get(DcMotor.class, "front_right");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding front_right, is it setup correctly?");
        }

        try {
            back_left = hardwareMap.get(DcMotor.class, "back_left");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_left, is it setup correctly?");
        }

        try {
            back_right = hardwareMap.get(DcMotor.class, "back_right");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_right, is it setup correctly?");
        }

        try {
            arm_raise = hardwareMap.get(DcMotor.class, "arm_raise");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_raise, is it setup correctly?");
        }

        try {
            duck_wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding duck_wheel, is it setup correctly?");
        }

        try {
            gripper = hardwareMap.get(Servo.class, "gripper");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding gripper servo, is it setup correctly?");
        }
        telemetry.update();

        while (opModeIsActive()) {

            // trying to get the arm_raise motor to lift the arm up and down
            if (arm_raise  != null) {
                arm_raise.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                if (gamepad1.a) {
                    arm_raise.setTargetPosition(360);
                    arm_raise.setPower(.2);
                }

                arm_raise.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            // right joy stick up and down will move arm up and down
            // a is top,
            // b is bottom level
            // x is to put it in the middle level

            // gamepad controllers
            double twist  = (gamepad1.right_trigger - gamepad1.left_trigger) * pivot_mod;
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;


            // all arm power
            if (gripper != null) {
                if (gamepad1.y) {
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
                if (gamepad1.right_bumper) {
                    duck_wheel.setPower(1 * duck_mod);
                } else {
                    duck_wheel.setPower(0);
                }
            }


            // wheel movement, makes sure all of the motors exist, shouldn't slow anything down
            if (front_left != null && front_right != null && back_left != null && back_right != null) {
                front_right.setPower((y - x + twist) * wheel_mod);
                front_left.setPower((y + x - twist) * wheel_mod);
                back_right.setPower((y + x + twist) * wheel_mod);
                back_left.setPower((y - x - twist) * wheel_mod);
            }


            // all new dope telemetry
            telemetry.addData("x_power: ", x);
            telemetry.addData("y_power: ", y);
            telemetry.addData("twist: ", twist);
            telemetry.update();
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
