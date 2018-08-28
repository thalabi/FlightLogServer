package com.kerneldc.flightlogserver.batch.copyMakeModelTable;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.batch.item.ItemProcessor;

import com.kerneldc.flightlogserver.domain.makeModel.MakeModel;

public class MakeModelItemProcessor implements ItemProcessor<MakeModel, MakeModel> {

	@Override
	public MakeModel process(MakeModel item) throws Exception {
		MakeModel result = new MakeModel();
		BeanUtils.copyProperties(/* destination */result, /* source */item);
		return result;
	}
}

