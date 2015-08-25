package br.uece.lotus.tcg.stopcondition;

abstract public class StopCondition{

    private String mName;
    private String mDescription;
    private String mPreText;
    private String mPostText;
    private String mDefaultValue;

    abstract void startMonitoring();

    abstract boolean satisfied();

    public String getName(){
        return mName;
    }

    protected void setName(String mName){
        this.mName = mName;
    }

    public String getDescription(){
        return mDescription;
    }

    protected void setDescription(String mDescription){
        this.mDescription = mDescription;
    }

    public String getPreText(){
        return mPreText;
    }

    protected void setPreText(String mPreText){
        this.mPreText = mPreText;
    }

    public String getPostText(){
        return mPostText;
    }

    protected void setPostText(String mPostText){
        this.mPostText = mPostText;
    }

    public String getDefaultValue(){
        return mDefaultValue;
    }

    abstract void setParameterValue(String value);

    protected void setDefaultValue(String mDefaultValue){
        this.mDefaultValue = mDefaultValue;
    }

    @Override
    public String toString(){
        return getName();
    }
}
