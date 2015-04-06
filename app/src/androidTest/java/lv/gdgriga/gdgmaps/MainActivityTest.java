package lv.gdgriga.gdgmaps;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import com.robotium.solo.Solo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testButtonClickedTextViewTextShouldBeHello() {
        TextView textView = (TextView) solo.getView(R.id.textView);
        Button okButton = (Button) solo.getView(R.id.okButton);

        solo.clickOnView(okButton);
        solo.waitForText("Hello!", 1, 10);
        CharSequence text = textView.getText();

        assertThat(text.toString(), is(equalTo("Hello!")));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}