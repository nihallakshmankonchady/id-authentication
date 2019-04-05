/* 
 * Copyright
 * 
 */
package io.mosip.preregistration.application.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.preregistration.application.dto.DeletePreRegistartionDTO;
import io.mosip.preregistration.application.dto.DemographicCreateResponseDTO;
import io.mosip.preregistration.application.dto.DemographicRequestDTO;
import io.mosip.preregistration.application.dto.DemographicUpdateResponseDTO;
import io.mosip.preregistration.application.dto.PreRegistrationViewDTO;
import io.mosip.preregistration.application.service.DemographicService;
import io.mosip.preregistration.core.common.dto.DemographicResponseDTO;
import io.mosip.preregistration.core.common.dto.MainListResponseDTO;
import io.mosip.preregistration.core.common.dto.MainRequestDTO;
import io.mosip.preregistration.core.common.dto.MainResponseDTO;
import io.mosip.preregistration.core.common.dto.PreRegIdsByRegCenterIdDTO;
import io.mosip.preregistration.core.common.dto.PreRegistartionStatusDTO;
import io.mosip.preregistration.core.config.LoggerConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class provides different API's to perform operations on
 * pre-registration.
 * 
 * @author Rajath KR
 * @author Sanober Noor
 * @author Tapaswini Behera
 * @author Jagadishwari S
 * @author Ravi C Balaji
 * @since 1.0.0
 */

@RestController
@RequestMapping("/")
@Api(tags = "Pre-Registration")
@CrossOrigin("*")
public class DemographicController {

	/** Autowired reference for {@link #DemographicService}. */
	@Autowired
	private DemographicService preRegistrationService;

	private Logger log = LoggerConfiguration.logConfig(DemographicController.class);

	/**
	 * Post API to create a pre-registation application.
	 *
	 * @param jsonObject
	 *            the json object
	 * @return List of response dto containing pre-id and group-id
	 */

	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@PostMapping(path = "/applications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create form data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Pre-Registration successfully Created"),
			@ApiResponse(code = 400, message = "Unable to create the Pre-Registration data") })
	public ResponseEntity<MainListResponseDTO<DemographicCreateResponseDTO>> register(
			@RequestBody(required = true) MainRequestDTO<DemographicRequestDTO> jsonObject) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for add preregistration with json object" + jsonObject);
		return ResponseEntity.status(HttpStatus.OK).body(preRegistrationService.addPreRegistration(jsonObject));
	}

	/**
	 * Put API to update a pre-registation application.
	 *
	 * @param jsonObject
	 *            the json object
	 * @return List of response dto containing pre-id and group-id
	 */

	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@PutMapping(path = "/applications/{preRegistrationId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update form data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Demographic data successfully Updated"),
			@ApiResponse(code = 400, message = "Unable to update the demographic data") })
	public ResponseEntity<MainListResponseDTO<DemographicUpdateResponseDTO>> update(
			@PathVariable("preRegistrationId") String preRegistrationId,
			@RequestBody(required = true) MainRequestDTO<DemographicRequestDTO> jsonObject) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for Update preregistration with json object" + jsonObject);
		return ResponseEntity.status(HttpStatus.OK)
				.body(preRegistrationService.updatePreRegistration(jsonObject, preRegistrationId));
	}

	/**
	 * Get API to fetch all the Pre-registration data for a pre-id.
	 *
	 * @param preRegId
	 *            the pre reg id
	 * @return the application data for a pre-id
	 */


	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@GetMapping(path = "/applications/{preRegistrationId}", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "Get Pre-Registartion data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Demographic data successfully retrieved"),
			@ApiResponse(code = 400, message = "Unable to get the demographic data") })
	public ResponseEntity<MainListResponseDTO<DemographicResponseDTO>> getApplication(
			@PathVariable("preRegistrationId") String preRegistraionId) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for fetching all demographic data with preregistartionId"
						+ preRegistraionId);
		return ResponseEntity.status(HttpStatus.OK).body(preRegistrationService.getDemographicData(preRegistraionId));
	}

	/**
	 * Put API to update the status of the application.
	 *
	 * @param preRegId
	 *            the pre reg id
	 * @param status
	 *            the status
	 * @return the updation status of application for a pre-id
	 */


	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@PutMapping(path = "/applications/status/{preRegistrationId}", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "Update Pre-Registartion status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Pre-Registration Status successfully updated"),
			@ApiResponse(code = 400, message = "Unable to update the Pre-Registration") })
	public ResponseEntity<MainResponseDTO<String>> updateApplicationStatus(
			@PathVariable("preRegistrationId") String preRegId,
			@RequestParam(value = "statusCode", required = true) String status) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for fetching all demographic data with preRegId " + preRegId
						+ " and status " + status);
		return ResponseEntity.status(HttpStatus.OK)
				.body(preRegistrationService.updatePreRegistrationStatus(preRegId, status));
	}

	/**
	 * Post api to fetch all the applications created by user.
	 *
	 * @param userId
	 *            the user id
	 * @return List of applications created by User
	 */


	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@GetMapping(path = "/applications", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "Fetch all the applications created by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All applications fetched successfully"),
			@ApiResponse(code = 400, message = "Unable to fetch applications ") })
	public ResponseEntity<MainListResponseDTO<PreRegistrationViewDTO>> getAllApplications(HttpServletRequest res) {
		String userId = preRegistrationService.authUserDetails().getUserId();
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for fetching all applications with userId " + userId + " Header "
						+ res.getHeader("Authorization"));
		return ResponseEntity.status(HttpStatus.OK).body(preRegistrationService.getAllApplicationDetails(userId));
	}

	/**
	 * Post API to fetch the status of a application.
	 *
	 * @param preId
	 *            the pre id
	 * @return status of application
	 */


	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@GetMapping(path = "/applications/status/{preRegistrationId}", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "Fetch the status of a application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All applications status fetched successfully"),
			@ApiResponse(code = 400, message = "Unable to fetch application status ") })
	public ResponseEntity<MainListResponseDTO<PreRegistartionStatusDTO>> getApplicationStatus(
			@PathVariable("preRegistrationId") String preId) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for fetching all applicationStatus with preId " + preId);
		return ResponseEntity.status(HttpStatus.OK).body(preRegistrationService.getApplicationStatus(preId));
	}

	/**
	 * Delete API to delete the Individual applicant and documents associated with
	 * the PreId.
	 *
	 * @param preId
	 *            the pre id
	 * @return the deletion status of application for a pre-id
	 */


	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")

	@DeleteMapping(path = "/applications/{preRegistrationId}", produces = MediaType.APPLICATION_JSON_VALUE)

	@ApiOperation(value = "Discard individual")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Deletion of individual is successfully"),
			@ApiResponse(code = 400, message = "Unable to delete individual") })
	public ResponseEntity<MainListResponseDTO<DeletePreRegistartionDTO>> discardIndividual(
			@PathVariable("preRegistrationId") String preId) {
		log.info("sessionId", "idType", "id",
				"In pre-registration controller for deletion of individual with preId " + preId);

		return ResponseEntity.status(HttpStatus.OK).body(preRegistrationService.deleteIndividual(preId));
	}

	@PreAuthorize("hasAnyRole('INDIVIDUAL','REGISTRATION_OFFICER','REGISTRATION_SUPERVISOR','REGISTRATION_ ADMIN')")
	@PostMapping(path = "/applications/updatedTime", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Updated Date Time for List of Pre-Registration Id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Updated Date Time successfully fetched for list of pre-registration ids"),
			@ApiResponse(code = 400, message = "Unable to the Updated Date Time") })
	public ResponseEntity<MainResponseDTO<Map<String, String>>> getUpdatedDateTimeByPreIds(
			@RequestBody MainRequestDTO<PreRegIdsByRegCenterIdDTO> mainRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(preRegistrationService.getUpdatedDateTimeForPreIds(mainRequestDTO.getRequest()));
	}

}
