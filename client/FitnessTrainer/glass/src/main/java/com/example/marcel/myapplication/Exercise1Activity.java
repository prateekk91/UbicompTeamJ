package com.example.marcel.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by Marcel on 11/20/2016.
 */

public class Exercise1Activity extends Activity {

    private static final String SERVER = "ws://137.110.91.71:8181";
    private static final String IDEAL_BODY_JOINTS_KEY = "idealBodyJoints";
    private static final String TRACKED_BODY_JOINTS_KEY = "trackedBodyJoints";
    private static final String DEVIATED_JOINT_NUMBER_KEY = "deviatedJointNumber";
    private static final String DEVIATED_JOINT_NAME_KEY = "deviatedJointName";
    private static final String SCALE_RATIO_KEY = "scaleRatio";
    private static final String DEVIATED_JOINT_ANGLE_KEY = "deviatedAngle";

    private WebSocketClient mWebSocketClient;
    private String exercise;
    private Bitmap idealBitmap;
    private Canvas idealCanvas;
    private Bitmap trackedBitmap;
    private Canvas trackedCanvas;
    private ImageView idealImageView;
    private ImageView trackedImageView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exercise = getIntent().getStringExtra("ExerciseName");
        setContentView(R.layout.activity_exercise1);
        idealImageView = (ImageView) findViewById(R.id.idealView);
        trackedImageView = (ImageView) findViewById(R.id.trackedView);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(exercise + " selected");
        connectWebSocket();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        idealBitmap = Bitmap.createBitmap(idealImageView.getHeight(), idealImageView.getWidth(), Bitmap.Config.ARGB_8888);
        idealCanvas = new Canvas(idealBitmap);

        trackedBitmap = Bitmap.createBitmap(trackedImageView.getHeight(), trackedImageView.getWidth(), Bitmap.Config.ARGB_8888);
        trackedCanvas = new Canvas(trackedBitmap);
    }

    private int getPositionFromJson(Object obj, String coordinate, double ratio, Pair<Double, Double> translatePosition) {
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject position = (JSONObject) jsonObj.get("Position");
        double value = Double.parseDouble(position.get(coordinate).toString()) * ratio;
        if (coordinate.equals("X")) {
            value += translatePosition.first - 110;
        } else {
            value += translatePosition.second - 35;
        }
        return (int) (value);
    }

    private void colorJoint(Paint paint, Canvas canvas, JSONObject joint, double scaleRatio, Pair<Double, Double> translatePosition) {
        try {;
            float x = getPositionFromJson(joint, "X", scaleRatio, translatePosition);
            float y = getPositionFromJson(joint, "Y", scaleRatio, translatePosition);
            canvas.drawCircle(x, y, 2, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIncorrectJointIndex(JSONArray trackedBodyJointsArr, int deviatedJointNumber) {
        for (int i = 0; i < trackedBodyJointsArr.size(); i++) {
            JSONObject jsonObj = (JSONObject) trackedBodyJointsArr.get(i);
            int joint = Integer.parseInt(jsonObj.get("JointType").toString());
            if (deviatedJointNumber == joint) {
                return i;
            }
        }
        return -1;
    }


    private void drawSkeleton(Paint paint, Canvas canvas, JSONArray bodyJoints, double scaleRatio, Pair<Double, Double> translatePosition) {
        try {
            paint.setColor(Color.BLUE);

            canvas.drawLine(getPositionFromJson(bodyJoints.get(0), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(0), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(1), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(1), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(1), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(1), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(15), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(15), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(15), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(15), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(3), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(3), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(15), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(15), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(2), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(2), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(1), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(1), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(4), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(4), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(2), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(2), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(6), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(6), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(3), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(3), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(5), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(5), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(5), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(5), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(7), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(7), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(6), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(6), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(8), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(8), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(11), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(11), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(13), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(13), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(12), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(12), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(14), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(14), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(9), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(9), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(11), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(11), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(10), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(10), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(12), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(12), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(4), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(4), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(9), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(9), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "Y", scaleRatio, translatePosition), paint);
            canvas.drawLine(getPositionFromJson(bodyJoints.get(10), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(10), "Y", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "X", scaleRatio, translatePosition), getPositionFromJson(bodyJoints.get(16), "Y", scaleRatio, translatePosition), paint);

            paint.setColor(Color.GREEN);
            for (int i = 0; i < bodyJoints.size(); i++) {
                JSONObject joint = (JSONObject) bodyJoints.get(i);
                colorJoint(paint, canvas, joint, scaleRatio, translatePosition);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pair<Double, Double> scaleBodiesOnView(JSONArray bodyJsonArr, double scaleFactor) {
        double translateFootX = 0;
        double translateFootY = 0;

        JSONObject jsonObj = (JSONObject) bodyJsonArr.get(13);
        JSONObject position = (JSONObject) jsonObj.get("Position");
        double footPositionX = Double.parseDouble(position.get("X").toString());
        double footPositionY = Double.parseDouble(position.get("Y").toString());

        jsonObj = (JSONObject) bodyJsonArr.get(14);
        position = (JSONObject) jsonObj.get("Position");
        if (Double.parseDouble(position.get("Y").toString()) > footPositionY) {
            footPositionX = Double.parseDouble(position.get("X").toString());
            footPositionY = Double.parseDouble(position.get("Y").toString());
        }
        
        double newFootPositionX = footPositionX * scaleFactor;
        double newFootPositionY = footPositionY * scaleFactor;
        
        translateFootX = footPositionX - newFootPositionX;
        translateFootY = footPositionY - newFootPositionY;
        
        return new Pair<Double, Double>(translateFootX, translateFootY);
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send(exercise);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);

                            idealCanvas.drawColor(Color.LTGRAY);
                            trackedCanvas.drawColor(Color.LTGRAY);

                            Paint paint = new Paint();
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(8);
                            paint.setAntiAlias(true);

                            double scaleRatio = 0.75;
                            double trackedScaleRatio = 1;

                            if (jsonObject.containsKey(IDEAL_BODY_JOINTS_KEY)) {
                                JSONArray idealBodyJointsArr = (JSONArray) jsonObject.get(IDEAL_BODY_JOINTS_KEY);
                                Pair<Double, Double> translatePosition = scaleBodiesOnView(idealBodyJointsArr, scaleRatio);
                                drawSkeleton(paint, idealCanvas, idealBodyJointsArr, scaleRatio, translatePosition);
                                idealImageView.setImageBitmap(idealBitmap);
                            }

                            if (jsonObject.containsKey(SCALE_RATIO_KEY)) {
                                trackedScaleRatio = Double.parseDouble(jsonObject.get(SCALE_RATIO_KEY).toString());
                            }

                            Log.d("Scale " , Double.toString(trackedScaleRatio));

                            if (jsonObject.containsKey(TRACKED_BODY_JOINTS_KEY)) {
                                JSONArray trackedBodyJointsArr = (JSONArray) jsonObject.get(TRACKED_BODY_JOINTS_KEY);
                                Pair<Double, Double> translatePosition = scaleBodiesOnView(trackedBodyJointsArr, scaleRatio * trackedScaleRatio);
                                drawSkeleton(paint, trackedCanvas, trackedBodyJointsArr, scaleRatio * trackedScaleRatio, translatePosition);
                                paint.setColor(Color.RED);
                                if (jsonObject.containsKey(DEVIATED_JOINT_NUMBER_KEY)) {
                                    int deviatedJointNumber = Integer.parseInt(jsonObject.get(DEVIATED_JOINT_NUMBER_KEY).toString());
                                    int incorrectJointIndex = getIncorrectJointIndex(trackedBodyJointsArr, deviatedJointNumber);
                                    if (incorrectJointIndex != -1) {
                                        colorJoint(paint, trackedCanvas, (JSONObject) trackedBodyJointsArr.get(incorrectJointIndex), scaleRatio * trackedScaleRatio, translatePosition);
                                    }
                                }
                                trackedImageView.setImageBitmap(trackedBitmap);
                            }

                            if (jsonObject.containsKey(DEVIATED_JOINT_NAME_KEY)) {
                                String jointName = jsonObject.get(DEVIATED_JOINT_NAME_KEY).toString();
                                textView.setText(jointName + " deviated by " + jsonObject.get(DEVIATED_JOINT_ANGLE_KEY).toString() + " degrees");
                            } else {
                                textView.setText("Hold the Posture");
                            }

                        } catch (ParseException e) {
                            textView.setText(message);
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Exercise1Activity.this.finish();
                                }
                            }, 2000);
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
    }

    @Override
    protected void onDestroy() {
        mWebSocketClient.close();
        mWebSocketClient = null;
        super.onDestroy();
    }
}