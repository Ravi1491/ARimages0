package com.example.arimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Collection;

//import com.google.ar.core.Anchor.image;

public class MainActivity extends AppCompatActivity implements Scene.OnUpdateListener {

    private CustumArFragment arFragment;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (CustumArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this);
    }

    public void setupDatabase(Config config, Session session) {

        Bitmap foxBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);

        aid.addImage( s="fox",foxBitmap);
        config.setAugmentedImageDatabase(aid);
    }

    @Override
    public void onUpdate(FrameTime frameTime) {

        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);

        for(AugmentedImage image : images){
            //com.google.ar.core.Anchor image;
            if(image.getTrackingState()== TrackingState.TRACKING)
                if(image.toString().equals("fox")){
                    Anchor anchor = (Anchor) image.createAnchor(image.getCenterPose());

                    createModel(anchor);
                    
                }
        }
    }

    private void createModel(Anchor anchor) {

        ModelRenderable.builder()
                .setSource(this, Uri.parse("ArcticFox_Posed.sfa"))
                .build()
                .thenAccept(modelRenderable -> placemodel(modelRenderable, anchor));
    }

    private void placemodel(ModelRenderable modelRenderable, Anchor anchor) {

        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }


    private class arimages {
    }

    private class Anchor extends com.google.ar.core.Anchor {
    }
}
