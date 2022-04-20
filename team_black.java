package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import java.util.Date;

import org.firstinspires.ftc.robotcore.external.Func;

@TeleOp(name = "Team Black Robnot", group = "best_opmode")
public class BlackRobot<motor> extends LinearOpMode {

    // modifications/power
    private static final double pivot_mod       = 1;
    private static final double wheel_mod       = .8;
    private static final double high_wheel_mod  = 1;
    private static final double duck_mod        = .15;
    private static final double arm_pivot_mod   = .3;
    private static final double arm_raise_mod   = .4;
    private static final double arm_raise2_mod  = .4;


    // moto declarations
    private DcMotor front_left  = null; // gobuilda move robot
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;


    // arm shananaginz
    private DcMotor arm_pivot   = null;
    private DcMotor duck_wheel  = null;
    private DcMotor arm         = null;
    private Servo   arm_claw    = null;

    
    boolean arm_up     = false;
    boolean y_toggle   = false;
    boolean arm_toggle = true;
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
            front_left = hardwareMap.get(DcMotor.class, "front_left");    // gobuilda move robot
            front_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);                  // make sure all motors spin the same speed
            front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);       // brakes when you stop instead of sliding around everywhere
        } catch (Exception e) {
            telemetry.addData(">", "Error Finding front_left, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            front_right = hardwareMap.get(DcMotor.class, "front_right");
            front_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
            back_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding back_right, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_pivot = hardwareMap.get(DcMotor.class, "arm_pivot");
            arm_pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm_pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_pivot, is it setup correctly?");
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
            arm = hardwareMap.get(DcMotor.class, "arm");
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm motor, is it setup correctly?");
            ++num_of_errors;
        }

        try {
            arm_claw = hardwareMap.get(Servo.class, "arm_claw");
        } catch (Exception e) {
            telemetry.addData(">","Error Finding arm_claw servo, is it setup correctly?");
            ++num_of_errors;
        }
        telemetry.update();

        long start_time = 0;

        waitForStart();

        while (opModeIsActive()) {

            // gamepad controls
            double twist  = Math.pow(gamepad1.right_trigger - gamepad1.left_trigger, 3) * pivot_mod;
            double y = Math.pow(gamepad1.left_stick_y, 3); // i did the math and it works properly
            double x = Math.pow(gamepad1.left_stick_x, 3);

            // all arm power
            if (arm_claw != null) {
                if (gamepad2.y) {
                    if (!arm_toggle) {
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


            // timing example
            if (arm != null) {
                Date date = new Date();
                if (gamepad2.a && arm_toggle && !arm_up) {
                    start_time = date.getTime();
                    arm.setPower(.6);
                    arm_up = true;
                    arm_toggle = false;
                }
                if (gamepad2.b && arm_toggle && arm_up) {
                    start_time = date.getTime();
                    arm.setPower(-.5);
                    arm_up = false;
                    arm_toggle = false;
                }
                if (date.getTime() - start_time >= 300 && !arm_toggle) {
                    arm.setPower(0);
                    arm_toggle = true;
                }
                if (arm_toggle) {
                    arm.setPower(gamepad2.right_stick_y * .4);
                }
            }
            

            // arm pivot
            if (arm_pivot != null) {
                arm_pivot.setPower(Math.pow(gamepad2.left_stick_x, 5) * arm_pivot_mod);
            }


            // duck power
            if (duck_wheel != null) {
                if (gamepad2.right_bumper) duck_wheel.setPower(1 * duck_mod);
                else if (gamepad2.left_bumper) duck_wheel.setPower(-1 * duck_mod);
                else duck_wheel.setPower(0);
            }


            // wheel movement, makes sure all of the motors exist, shouldn't slow anything down
            if (front_left != null && front_right != null && back_left != null && back_right != null) {
                front_right.setPower((y + x + twist) * wheel_mod);
                front_left.setPower((-y + x + twist) * wheel_mod);
                back_right.setPower((y - x + twist) * wheel_mod);
                back_left.setPower((y + x - twist) * wheel_mod);
            }

            // only update telemetry
            if (num_of_errors != 0) {
                telemetry.addData("x_power: ", x);
                telemetry.addData("y_power: ", y);
                telemetry.addData("twist: ", twist);
                telemetry.addData("pivot power", gamepad2.left_stick_x);
                telemetry.update();
            }
        }

        try {
            front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // reset encoders
        } catch (Exception e) {
            telemetry.addData(">", "Error resetting front_left's encoder, is it setup correctly?");
        }

        try {
            front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error resetting front_right's encoder, is it setup correctly?");
        }

        try {
            back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error resetting back_left's encoder, is it setup correctly?");
        }

        try {
            back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error resetting back_right's encoder, is it setup correctly?");
        }

        try {
            arm_pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error resetting arm_pivot's encoder, is it setup correctly?");
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
