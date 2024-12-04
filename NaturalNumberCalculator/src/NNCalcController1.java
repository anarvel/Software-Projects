import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author A.Narvel
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model, NNCalcView view) {

        NaturalNumber top = model.top();
        NaturalNumber bottom = model.bottom();
        view.updateTopDisplay(top);
        view.updateBottomDisplay(bottom);

        // enable subtract button if bottom <= top
        view.updateSubtractAllowed(bottom.compareTo(top) <= 0);
        // enable divide button if bottom is not zero
        view.updateDivideAllowed(!bottom.isZero());
        // enable power button if bottom is within int_limit range
        view.updatePowerAllowed(model.bottom().compareTo(INT_LIMIT) < 0);
        // enable root button if bottom is >= 2 and within int_limit range
        view.updateRootAllowed(model.bottom().compareTo(TWO) >= 0
                && model.bottom().compareTo(INT_LIMIT) < 0);
    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        // clear the bottom number in the model
        NaturalNumber bottom = this.model.bottom();
        bottom.clear();
        // update view
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        // swap top and bottom numbers
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        // update view
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {
        // copy bottom number to the top
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.copyFrom(bottom);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddEvent() {
        // add top number to the bottom and clear the top
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        bottom.add(top);
        top.clear();
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processSubtractEvent() {
        // subtract bottom number from top and move result to the bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.subtract(bottom);
        bottom.transferFrom(top);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processMultiplyEvent() {
        // multiply top and bottom and clear the top
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.multiply(bottom);
        top.clear();
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processDivideEvent() {
        // divide top number by the bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        // move quotient to the bottom and remainder to the top
        NaturalNumber remainder = top.divide(bottom);
        bottom.transferFrom(top);
        top.transferFrom(remainder);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processPowerEvent() {
        // raise the top number to the power of the bottom number
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        int exponent = bottom.toInt();
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        temp.power(exponent);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processRootEvent() {
        // find the root of the top number from the value of the bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        int root = bottom.toInt();
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        temp.root(root);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        // append a new digit to the bottom number
        NaturalNumber bottom = this.model.bottom();
        bottom.multiplyBy10(digit);
        // update view
        updateViewToMatchModel(this.model, this.view);

    }

}
