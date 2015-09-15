package br.uece.lotus.tcg.struct;

public class ResultInfo{

    private PathSet mPathSet;
    private String mGeneratorName;
    private String mSelectorName;
    private String mPrioritizerName;
    private String mTestPurpose;
    private String mMessage;

    public PathSet getPathSet(){
        return mPathSet;
    }

    public String getGeneratorName(){
        return mGeneratorName;
    }
    
    public String getSelectorName(){
        return mSelectorName;
    }

    public String getPrioritizerName(){
        return mPrioritizerName;
    }

    public String getTestPurpose(){
        return mTestPurpose;
    }

    public String getMessage(){
        return mMessage;
    }

    public ResultInfo(PathSet ps, String genName, String selName, String priName, String additionalMsg){
        
        mPathSet = ps;
        mGeneratorName = genName;
        mSelectorName = selName;
        mPrioritizerName = priName;
        mMessage = additionalMsg;
        
    }
}
