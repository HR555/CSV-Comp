package com.csvcomp.api.batch;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public interface Duplicates {

	public SetMultimap<String, String> duplicates = HashMultimap.create();
	public int duplicatesSize=0;

}
