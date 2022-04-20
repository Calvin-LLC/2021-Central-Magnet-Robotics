package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Func;

@TeleOp(name = "Team Gold Robnot autonomous right", group = "best_opmode")
public class GoldAutoRight<motor> extends LinearOpMode {

    // modifications/power
    private static final double pivot_mod       = 1;
    private static final double wheel_mod       = 1;
    private static final double duck_mod        = .25;


    // moto declarations
    private DcMotor front_left  = null; // gobuilda move robot
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;


    // arm shananaginz
    private DcMotor duck_wheel  = null;


    int num_of_errors = 0;
    
    public static void acc_sleep(long ms) {
        long start = System.nanoTime();
        long nanos = ms * 1000000;
        while (System.nanoTime() - start < nanos) {
            
        }
    }

    // give it a direction, 1,2,3,4 (NEVER EAT SOGGY WAFFLES)
    // 1: forwards
    // 2: right
    // 3: backwards
    // 4: left
    // 5: rotate right
    // 6: rotate left
    // time in MS that it moves in that direction for, behavior should brake afterwards
    public void move(int direction, double speed_modifier, long time) {
        double x = 0, y = 0, twist = 0;

        switch (direction) {
            case 1:
                y = 1;
                break;
            case 2:
                x = 1;
                break;
            case 3:
                y = -1;
                break;
            case 4:
                x = -1;
                break;
            case 5:
                twist = 1;
                break;
            case 6:
                twist = -1;
                break;
            default:
                break;
        }

        if (front_left != null && front_right != null && back_left != null && back_right != null) {
            front_right.setPower((y - x + twist) * speed_modifier);
            front_left.setPower((-y - x + twist) * speed_modifier);
            back_right.setPower((y + x + twist) * speed_modifier);
            back_left.setPower((-y + x + twist) * speed_modifier);
        } else telemetry.addData("Error finding all motors!", null);

        acc_sleep(time);

        if (front_left != null && front_right != null && back_left != null && back_right != null) {
            front_right.setPower(0);
            front_left.setPower(0);
            back_right.setPower(0);
            back_left.setPower(0);
        } else telemetry.addData("Error finding all motors!", null);
        telemetry.update();
    }

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
            duck_wheel = hardwareMap.get(DcMotor.class, "duck_wheel");
            duck_wheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch (Exception e) {
            telemetry.addData(">","Error Finding duck_wheel, is it setup correctly?");
            ++num_of_errors;
        }

        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            move(1, .5, 280);
            acc_sleep(200);
            move(4, .4, 1700);
            acc_sleep(200);
            duck_wheel.setPower(-.27);
            acc_sleep(10000);
            duck_wheel.setPower(0);
            move(1, .45, 975);
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
            duck_wheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } catch (Exception e) {
            telemetry.addData(">","Error resetting duck_wheel's encoder, is it setup correctly?");
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
