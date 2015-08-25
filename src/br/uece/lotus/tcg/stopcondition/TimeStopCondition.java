package br.uece.lotus.tcg.stopcondition;

class TimeStopCondition extends StopCondition{

    private long mStartTime;
    private long mLimitTime;

    TimeStopCondition(){
        
        setName("Time Limit");
        setDescription("Specify the maximum time that the generator can spend in the process of test case generaiton");
        setPostText("");
        setPreText("Time (secs):");
        setDefaultValue("60");
        mLimitTime = 60000;
        
    }

    @Override
    void startMonitoring(){
        mStartTime = System.currentTimeMillis();
    }

    @Override
    boolean satisfied(){
        long diff = System.currentTimeMillis() - mStartTime;
        return diff > mLimitTime;
    }

    @Override
    void setParameterValue(String value){
        mLimitTime = Long.parseLong(value) * 1000;
    }
}
