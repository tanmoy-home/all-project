package com.rssoftware.ou.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bbps.schema.Ack;
import org.bbps.schema.ErrorMessage;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.model.tenant.AgentView;

@Component
public class AgentValidator {

	public boolean isValidPayChannelnMode(AgentView agent, List<String> paymentChannels, List<String> paymentModes) throws ValidationException {
		boolean validInputs = true;
		List<ErrorMessage> errors = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(paymentChannels)) {
			validatePaymentChannels(agent, paymentChannels, errors, validInputs);
		}
		if (CollectionUtils.isNotEmpty(paymentModes)) {
			validatePaymentModes(agent, paymentModes, validInputs, errors);
		}

		if (CollectionUtils.isNotEmpty(errors)) {
			Ack ack = new Ack();
			ack.getErrorMessages().addAll(errors);
			throw ValidationException.getInstance(ack);
		}
		return validInputs;
	}

	private void validatePaymentModes(AgentView agent, List<String> paymentModes, boolean validInputs,
			List<ErrorMessage> errors) {
		List<String> agentPaymentModes = new ArrayList<>();

		boolean agentHasAllPayModes = CollectionUtils.isNotEmpty(agent.getAgentPaymentModes()) ? CommonUtils
				.getPaymentModesAsStrings(agent.getAgentPaymentModes(), agentPaymentModes).containsAll(paymentModes)
				: false;
		if (!agentHasAllPayModes) {
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.PAYMENT_MODE_NOT_IN_AGENT_PROFILE.name());
			error.setErrorDtl(
					ValidationException.ValidationErrorReason.PAYMENT_MODE_NOT_IN_AGENT_PROFILE.getDescription());
			errors.add(error);
			validInputs = false;
		}

	}

	private void validatePaymentChannels(final AgentView agent, final List<String> paymentChannels,
			List<ErrorMessage> errors, boolean validInputs) {
		if (!agent.isDummyAgent()) {
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.PAYMENT_CHANNEL_NOT_SUPPORTED.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.PAYMENT_CHANNEL_NOT_SUPPORTED.getDescription());
			errors.add(error);
			validInputs = false;
		} else {
			List<String> agentPaymentChannels = new ArrayList<>();

			boolean agentHasAllPayChannels = agent.getAgentPaymentChannels() != null
					? CommonUtils.getPaymentChannelsAsStrings(agent.getAgentPaymentChannels(), agentPaymentChannels)
							.containsAll(paymentChannels)
					: false;
			if (!agentHasAllPayChannels) {
				ErrorMessage error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.PAYMENT_CHANNEL_NOT_IN_AGENT_PROFILE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.PAYMENT_CHANNEL_NOT_IN_AGENT_PROFILE
						.getDescription());
				errors.add(error);
				validInputs = false;
			}
		}
	}

	public boolean isValidAgent(AgentView agent) {
		boolean validagent;
		if (agent == null) {
			validagent = false;
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.AGENT_NOT_FOUND.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.AGENT_NOT_FOUND.getDescription());
			//response.getErrors().add(error);
		} else {
			validagent = true;
		}

		return validagent;
	}
}
