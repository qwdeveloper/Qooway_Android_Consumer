package ui;
import com.example.consumerv01.R;
import com.example.consumerv01.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
	 

	public class MainActivity extends Activity {
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
	         
	        Thread logoTimer = new Thread() {
	            public void run(){
	                try{
	                    int logoTimer = 0;
	                    while(logoTimer < 2000){
	                        sleep(100);
	                        logoTimer = logoTimer +100;
	                    };
	                    startActivity(new Intent("com.example.consumerv01.CLEARSCREEN"));
	                } 
	                 
	                catch (InterruptedException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	                 
	                finally{
	                    finish();
	                }
	            }
	        };
	         
	        logoTimer.start();
	    }
	}

