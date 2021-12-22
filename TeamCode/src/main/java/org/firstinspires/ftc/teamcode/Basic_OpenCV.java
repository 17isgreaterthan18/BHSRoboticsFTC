package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.*;


@TeleOp(name = "Basic: OpenCV", group="???")
public class Basic_OpenCV extends OpMode
{
    OpenCvCamera camera;
    @Override
    public void init() {
        telemetry.addData("Status", "Running");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        // TODO: Set pipeline here.
        camera.setPipeline(new FTCPipeline());

//        camera.setMillisecondsPermissionTimeout(2500);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Start streaming camera here.
                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                // lol no work like rishi muscles
                System.out.println("Camera - Not working");
            }
        });
        telemetry.addLine("Waiting to start.");
        telemetry.update();


    }

    @Override
    public void loop() {
        telemetry.addData("Frame Count", camera.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", camera.getFps()));
        telemetry.addData("Total frame time ms,", camera.getTotalFrameTimeMs());
        telemetry.update();
        if(gamepad1.a)
        {
            camera.stopStreaming();
        }
        try {
            Thread.sleep(100);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    class FTCPipeline extends OpenCvPipeline
    {
        boolean viewportPaused;
        @Override
        public Mat processFrame(Mat input) {
            Imgproc.rectangle(
                    input,
                    new Point(
                            input.cols()/4,
                            input.rows()/4),
                    new Point(
                            input.cols()*(3f/4f),
                            input.rows()*(3f/4f)),
                    new Scalar(0,255,0),4);
            return input;
        }

        @Override
        public void onViewportTapped()
        {
            viewportPaused = !viewportPaused;

            if(viewportPaused)
            {
                camera.pauseViewport();
            }
            else
            {
                camera.resumeViewport();
            }
        }
    }
}