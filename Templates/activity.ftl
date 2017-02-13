package ${package};

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
${imports}

public class ${activity} extends AppCompatActivity {

${variables}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_${layout});
${onCreate}
	}

${methods}

}
