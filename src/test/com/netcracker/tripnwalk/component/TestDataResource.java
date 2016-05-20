package com.netcracker.tripnwalk.component;

import com.github.springtestdbunit.dataset.FlatXmlDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.springframework.core.io.Resource;

public class TestDataResource  extends FlatXmlDataSetLoader {
    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        return createReplacementDataSet(super.createDataSet(resource));
    }
    private ReplacementDataSet createReplacementDataSet(IDataSet dataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
        replacementDataSet.addReplacementObject("[null]", null);
        return replacementDataSet;
    }
}
