package dk.d3m.dbs.model;

/**
 * Created by Patrick on 28-08-2014.
 */
public class RegisterHandler {

    private static SaleRegister saleRegisterInstance;
    private static PictureRegister pictureRegisterInstance;

    private RegisterHandler() {}

    public static SaleRegister getSaleRegisterInstance() {
        if(saleRegisterInstance != null) {
            return saleRegisterInstance;
        } else {
            makeSaleRegisterInstance();
            return saleRegisterInstance;
        }
    }

    private static void makeSaleRegisterInstance() {
        saleRegisterInstance = new SaleRegister(getPictureRegisterInstance());
    }

    public static PictureRegister getPictureRegisterInstance() {
        if(pictureRegisterInstance != null) {
            return pictureRegisterInstance;
        } else {
            makePictureRegisterInstance();
            return pictureRegisterInstance;
        }
    }

    private static void makePictureRegisterInstance() {
        pictureRegisterInstance = new PictureRegister();
    }
}
