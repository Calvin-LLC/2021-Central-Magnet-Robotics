package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/* FTC Documentation & resources: */

/* ctrl click on the links to go to them
 * season info: https://www.firstinspires.org/robotics/ftc/game-and-season
 * ftc documentation: https://ftctechnh.github.io/ftc_app/doc/javadoc/index.html
 * simple setup guide: https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/android-studio-guide.pdf*/

// you can look in FtcRobotController->java->org.firstinspires.ftc.robotcontroller->external.samples for examples

@TeleOp(name = "motor test", group = "best_opmode")
public class motor_test<motor> extends LinearOpMode {


    /* Drive vars, final since we only assign it once! */
    //private final DcMotor frontLeft    = (DcMotor) hardwareMap.get(Servo.class, "frontLeft");          // Front left wheel motor
    //private final DcMotor frontRight   = (DcMotor) hardwareMap.get(Servo.class, "frontRight");         // Front right wheel motor
    //private final DcMotor backLeft     = (DcMotor) hardwareMap.get(Servo.class, "backLeft");           // Back left wheel motor
    //private final DcMotor backRight    = (DcMotor) hardwareMap.get(Servo.class, "backRight");          // Back right wheel motor
    //private final DcMotor belt         = (DcMotor) hardwareMap.get(Servo.class, "belt");               // Belt motor
    private DcMotor motor = null;
    private Servo claw = null;
    private float spin_power = 0.30f; // The spin power
    private boolean dpad = false;     // A boolean used to indicate whether the dpad has been pressed used for the changing of spin power

    @Override   // we're over-riding their function to run our code instead of ftc's!
    public void runOpMode() {

        float x = 0;
        boolean boss = false;
        waitForStart();
        while (opModeIsActive()) {
            /* Since joystick input is constantly changing, we want to update it as fast as possible,
             * that's why we're running it in a loop! */
            // The left trigger input, used to spin CCW
            motor = hardwareMap.get(DcMotor.class, "motor");
            claw  = hardwareMap.get(Servo.class, "claw");


            float pivotL = gamepad1.left_trigger * gamepad1.left_trigger * Math.abs(gamepad1.left_trigger) * 0.85f;
            // The right trigger input, used to spin CW
            float pivotR = gamepad1.right_trigger * gamepad1.right_trigger * Math.abs(gamepad1.right_trigger) * 0.85f;
            // The joystick Y input, used as the forward and backward power
            float power = -gamepad1.left_stick_y * gamepad1.left_stick_y * gamepad1.left_stick_y;
            // The joystick X input, used to strafe side to side
            float strafe = gamepad1.left_stick_x * gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x);


            /* Set wheel power based on stick and trigger input */
            /*frontLeft.setPower(Pwr - strafe + PivotL - PivotR);
            frontRight.setPower(-Pwr - strafe + PivotL - PivotR);
            backLeft.setPower(Pwr + strafe + PivotL - PivotR);
            backRight.setPower(-Pwr + strafe + PivotL - PivotR);*/

            //claw.setPosition(gamepad1.right_stick_x);
/*
            if (0 <= gamepad1.right_stick_x && gamepad1.right_stick_x <= 1) {
                x = gamepad1.right_stick_x;
                claw.setPosition(x * 10);
            }*/

            if(gamepad1.y) {
                if (!boss) {
                    claw.setPosition(-claw.getPosition());
                    boss = true;
                } else {
                    claw.setPosition(1.0);
                    telemetry.addData("claw opened", "!");
                }
            } else {
                boss = false;
                claw.setPosition(0.0);
            }



            /* Set spin power using dpad */
            if (gamepad1.dpad_up) {
                if (!dpad) {
                    spin_power += 0.02f;
                    dpad = true;
                }
            } else if (gamepad1.dpad_down) {
                if (!dpad) {
                    spin_power -= 0.02f;
                    dpad = true;
                }
            } else dpad = false;

            /* Adding Telemetry Data (for debugging) */
            telemetry.addData("PivotL: ", pivotL);
            telemetry.addData("PivotR: ", pivotR);
            telemetry.addData("claw: ", gamepad1.right_stick_x);
            telemetry.addData("power: ", power);
            telemetry.addData("Spin Power", Math.round(spin_power * 100));
            telemetry.update();

            motor.setPower(power * 2);
        }
    }
}
