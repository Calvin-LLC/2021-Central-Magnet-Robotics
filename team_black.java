package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Func;

@TeleOp(name = "Team Black Robnot", group = "best_opmode")
public class black_robnot<motor> extends LinearOpMode {

    // modifications/power
    private static final double pivot_mod       = 1;
    private static final double wheel_mod       = 1;
    private static final double high_wheel_mod  = 1;
    private static final double duck_mod        = .25;
    private static final double arm_out_mod     = .7;
    private static final double arm_pivot_mod   = .5;


    // moto declarations
    private DcMotor front_left  = null; // gobuilda move robot
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;


    // arm shananaginz
    private DcMotor arm_pivot   = null;
    private DcMotor duck_wheel  = null;
    private Servo   arm_claw    = null;
    private Servo   arm_y1      = null;
    private Servo   arm_y2      = null;


    boolean y_toggle = false;
    int num_of_errors = 0;

    @Override   // we're over-riding their function to run our code instead of ftc's!
    public void runOpMode() {
        // shows the current battery life in volts whenever you run opmode
        telemetry.addData("voltage", "%.1f volts", new Func<Double>() {
            @Override public Double value() {
                return getBatteryVoltage();
            }
        });

        telemetry.addData(">", "Waiting For Start");

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
            arm_pivot = hardwareMap.get(DcMotor.class, "arm_pivot");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_pivot, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            duck_wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding duck_wheel, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_y1 = hardwareMap.get(Servo.class, "arm_y1");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_y1 servo, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_y2 = hardwareMap.get(Servo.class, "arm_y2");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_y2 servo, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_claw = hardwareMap.get(Servo.class, "gripper");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_claw servo, is it setup correctly?");
            ++num_of_errors;
        }
        telemetry.update();
        num_of_errors = 0;

        arm_pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();

        while (opModeIsActive()) {
            // gamepad controls
            double twist  = (gamepad1.right_trigger - gamepad1.left_trigger) * pivot_mod;
            double y = gamepad1.left_stick_y * (1 / .707); // 1 / sin(45)
            double x = gamepad1.left_stick_x * (1 / .707); // 1 / sin(45)


            // movement curve
            if (y > .8) {
                y *= high_wheel_mod;
            } else {
                y *= wheel_mod;
            }
            if (x > .8) {
                x *= high_wheel_mod;
            } else {
                x *= wheel_mod;
            }

            // all arm power
            if (arm_claw != null) {
                if (gamepad2.y) {
                    if (!y_toggle) {
                        arm_claw.setPosition(-arm_claw.getPosition());
                        y_toggle = true;
                    } else {
                        arm_claw.setPosition(1.0);
                    }
                } else {
                    y_toggle = false;
                    arm_claw.setPosition(0);
                }
            }

            // arm out
            if (arm_y1 != null && arm_y2 != null) {

            }

            // arm pivot and power
            if (arm_pivot != null) {
                arm_pivot.setPower(gamepad2.left_stick_x * arm_pivot_mod);
            }

            // duck power
            if (duck_wheel != null) {
                if (gamepad2.right_bumper) {
                    duck_wheel.setPower(1 * duck_mod);
                } else if (gamepad2.left_bumper) {
                    duck_wheel.setPower(1 * duck_mod);
                } else {
                    duck_wheel.setPower(0);
                }
            }


            // wheel movement, makes sure all of the motors exist, shouldn't slow anything down
            if (front_left != null && front_right != null && back_left != null && back_right != null) {
                front_right.setPower((y + x + twist) * wheel_mod);
                front_left.setPower((y - x - twist) * wheel_mod);
                back_right.setPower(-(y - x + twist) * wheel_mod);
                back_left.setPower((y + x - twist) * wheel_mod);
            }

            // only update telemetry
            if (num_of_errors == 0) {
                telemetry.addData("x_power: ", x);
                telemetry.addData("y_power: ", y);
                telemetry.addData("twist: ", twist);
                telemetry.addData("pivot power", gamepad2.left_stick_x);
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
