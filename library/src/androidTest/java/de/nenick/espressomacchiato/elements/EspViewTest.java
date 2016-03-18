package de.nenick.espressomacchiato.elements;

import android.support.test.espresso.NoMatchingViewException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;

import de.nenick.espressomacchiato.test.views.BaseActivity;
import de.nenick.espressotools.EspressoTestCase;

public class EspViewTest extends EspressoTestCase<BaseActivity> {

    private static final String VIEW_WAS_CLICKED_MESSAGE = "view was clicked";

    private int viewId = android.R.id.button1;
    private EspView espView = EspView.byId(viewId);
    private Button view;

    private int messageViewId = android.R.id.text1;
    private TextView messageView;
    private EspTextView espTextView = EspTextView.byId(messageViewId);

    @Test
    public void testAssertions() {
        givenClickableView();

        espView.assertIsVisible();
        espView.assertIsEnabled();

        givenViewIsDisabled();
        espView.assertIsDisabled();

        givenViewIsHidden();
        espView.assertIsHidden();
    }

    @Test
    public void testClick() {
        givenClickableView();
        givenClickFeedbackTextView();

        espView.click();
        espTextView.assertTextIs(VIEW_WAS_CLICKED_MESSAGE);
    }

    @Test
    public void testClickFailureWhenNotVisible() {
        exception.expect(NoMatchingViewException.class);
        exception.expectMessage("No views in hierarchy found matching: (with id: android:id/button1 and is displayed on the screen to the user)");

        givenClickableView();
        view.setVisibility(View.INVISIBLE);
        espView.click();
    }

    @Test
    public void testClickSelectsOnlyVisibleView() {
        givenClickableView();
        view.setVisibility(View.INVISIBLE);
        givenClickableView();
        givenClickFeedbackTextView();

        espView.click();
        espTextView.assertTextIs(VIEW_WAS_CLICKED_MESSAGE);
    }

    private void givenViewIsHidden() {
        performOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void givenViewIsDisabled() {
        performOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(false);
            }
        });
    }

    private void givenClickableView() {
        view = new Button(activityTestRule.getActivity());
        view.setId(viewId);
        addViewToActivity(view, BaseActivity.rootLayout);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageView.setText(VIEW_WAS_CLICKED_MESSAGE);
            }
        });
    }

    private void givenClickFeedbackTextView() {
        messageView = new TextView(activityTestRule.getActivity());
        messageView.setId(messageViewId);
        addViewToActivity(messageView, BaseActivity.rootLayout);
    }
}