package com.rssoftware.ou.service;

import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;

public interface CBSService {

	void initialize();

	CBSResponse doBalanceEnquiry(CBSRequest request);

	CBSResponse doDebit(CBSRequest request);

	CBSResponse doCredit(CBSRequest request);

	CBSResponse doDebitReversal(CBSRequest request);

	CBSResponse doCreditReversal(CBSRequest request);
}
