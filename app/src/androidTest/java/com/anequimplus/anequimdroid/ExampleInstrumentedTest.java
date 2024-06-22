package com.anequimplus.anequimdroid;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.anequimplus.utilitarios.ConfiguracaoCloudNFceNFCe;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.anequim.anequimdroid", appContext.getPackageName());
    }

    @Test
    public void testeNumeroNFce(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


        assertEquals(ConfiguracaoCloudNFceNFCe.getLinkCloudNFce(appContext), 3);

    }


}
