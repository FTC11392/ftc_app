package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.DriveMecanumWW;
import org.firstinspires.ftc.teamcode.subsystem.IMU;
import org.firstinspires.ftc.teamcode.subsystem.LandingLiftREV;

@Autonomous(name="Drive Only Auto", group="test")
@Disabled
public class DriveOnlyAuto extends LinearOpMode {

    private DriveMecanumWW tank;
    private ElapsedTime runtime = new ElapsedTime();

    //SamplingOrder gold;

    static final double     LANDING_SPEED = 0.3;
    static final double     FORWARD_SPEED = 0.4;
    static final double     STRAFE_SPEED  = 0.3;
    static final double     TURN_SPEED    = 0.2;

    @Override
    public void runOpMode() throws InterruptedException { //need throw for landing() method
        tank = new DriveMecanumWW(telemetry, hardwareMap);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way
        // step 1: recognize the gold
        runtime.reset();
        tank.encoderDrive(tank.backRight, 0.5, 120);
        tank.encoderDrive(tank.frontLeft, 0.5, 120);
        sleep(2000);


        tank.stop();
        tank.encoderMode();

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);

    }
}


