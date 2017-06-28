package urmc.drinkingapp.model;

import java.util.UUID;
/**
 * Created by litchiyang on 6/27/17.
 */

public class PhoneNumbers {

    private String mNumber;

    private UUID mId;

    public PhoneNumbers() {
        this(UUID.randomUUID());
    }

    public PhoneNumbers(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }


    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }


}
