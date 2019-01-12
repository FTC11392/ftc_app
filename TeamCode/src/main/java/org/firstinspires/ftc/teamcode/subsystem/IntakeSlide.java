package org.firstinspires.ftc.teamcode.subsystem;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class IntakeSlide extends Encoder {
    private DcMotor intakeFlip;
    private CRServo intake;
    private CRServo intake2;
    private DcMotor intakeOut;

    HardwareMap hwMap =  null;
    Telemetry telemetry;

    //arm expand and shrink
    public int ENDSTOP1;
    public int ENDSTOP2;
    public int c_position;
    public int c_target;
    public double c_speed;
    public String c_state;

    //arm start positions, where the countings start
    public int V_MIN = 0; //should be 0
    //arm is down, shrink, and at front when stop
    //private final int V_STOP = 5;
    //arm current up and down positions
    public boolean v_state_up;
    public int v_position;    //ticks
    public int v_target;
    public double v_speed;

    public IntakeSlide (HardwareMap hwMap, Telemetry telemetry) {
        /* Initialize standard Hardware interfaces */
        this.hwMap = hwMap;
        this.telemetry = telemetry;
    }

    public void init () {
        intake = hwMap.get(CRServo.class, "intake");
        intake2 = hwMap.get(CRServo.class, "intake2");

        intakeOut = hwMap.get(DcMotor.class, "intakeOut");
        intakeOut.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeOut.setPower(0.0);
        c_position = intakeOut.getCurrentPosition();
        c_target = c_position;
        c_state = "";
        ENDSTOP2 = c_position;
        ENDSTOP1 = ENDSTOP2 - 800;

        intakeFlip = hwMap.get(DcMotor.class, "intakeFlip");
        intakeFlip.setDirection(DcMotor.Direction.REVERSE);
        //v_pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //this behave not as expected
        //intakeFlip.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeFlip.setPower(0.0);
        v_state_up = true;
        v_position = intakeFlip.getCurrentPosition();
        v_target = v_position;
        V_MIN = v_position;
    }

    public void shrink() {
        if (c_state.equals("mOUT")) {
            c_target = intakeOut.getCurrentPosition();
            intakeOut.setTargetPosition(intakeOut.getCurrentPosition());
        }
        cmove(0.3,30);
        c_state = "mIN";
    }

    public void expand() {
        if (c_state.equals("mIN")) {
            c_target = intakeOut.getCurrentPosition();
            intakeOut.setTargetPosition(intakeOut.getCurrentPosition());
        }
        cmove(0.8,-80);
        c_state = "mOUT";
    }

    private boolean cmove(double speed, int change) {
        if (Math.abs(intakeOut.getCurrentPosition() - c_target) < 25) {
            c_target += change;
            c_speed = speed;
            return true;
        } else {
            return false;
        }
    }


    public void intake() {
        intake.setPower(-1);
    }
    public void intake2(){
        intake2.setPower(-1);
    }
    public void outtake() {
        intake.setPower(1);
    }
    public void outtake2(){
        intake2.setPower(1);
    }
    public void stopIntake() {
        intake.setPower(0);
    }
    public void stopIntake2() {
        intake2.setPower(0);
    }


    public void setIntakeFlip (double power) {
        intakeFlip.setPower(power);
    }

    //up and down ---------------------------------------------------------------------------------
    public void up() { //target is the ticks
        vmove(0.4,20);
    }

    public void down() {
        vmove(3,-20);
    }

    public void vhold() {
        v_speed = 0.8;
    }

    private boolean vmove(double speed, int change) {
        intakeFlip.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (Math.abs(intakeFlip.getCurrentPosition() - v_target) < 30) {
            v_target += change;
            v_speed = speed;
            return true;
        } else {
            return false;
        }
    }

    public void update() {
        //telemetry.addData("intakeFlip","speed: "+v_speed+" target: "+v_target);
        //runToPosition(intakeFlip, v_speed, v_target);

        c_target = Range.clip(c_target,ENDSTOP1, ENDSTOP2);
        telemetry.addData("intakeOut","speed: "+c_speed+" target: "+c_target + " position: " + intakeOut.getCurrentPosition());
        runToPosition(intakeOut, c_speed, c_target);
    }

    public void stopFlip() {
        //intakeFlip.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //intakeFlip.setTargetPosition(V_STOP);
        //intakeFlip.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //while (intakeFlip.isBusy()) {} //wait for moving down to finish
        intakeFlip.setPower(0.0); //?? should we rest v_pivot by
    }

    public void upFlip() {
        intakeFlip.setPower(0.7);
    }

    public void downFlip() {
        intakeFlip.setPower(-0.6);
    }
}
