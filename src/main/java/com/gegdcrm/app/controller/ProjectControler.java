package com.gegdcrm.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.Validation;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gegdcrm.app.exception.CustomException;
import com.gegdcrm.app.exception.ExceptionThrower;
import com.gegdcrm.app.model.ProjectBean;
import com.gegdcrm.app.repo.ProjectRep;

import javax.validation.ConstraintViolation; 
import javax.validation.Validator; 

/**
 *
 * @author charan kandula
 * @version 1.0
 * 
 * 
 */
@RestController
@RequestMapping("/bidder/v1")
public class ProjectControler {

	@Autowired
	ProjectRep bidderRep;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@SuppressWarnings("rawtypes")
	@PostMapping("/savebidder")
	ResponseEntity saveBidder(@RequestBody ProjectBean bidderBean) {
		logger.info("staring saveBidder");
		String errorMsg = "";
		
		Set<ConstraintViolation<ProjectBean>> constraintViolations = validator.validate(bidderBean);
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<ProjectBean> cv : constraintViolations) {
				errorMsg = errorMsg + cv.getMessage();
			}
			errorMsg = errorMsg + "$$";
			JSONObject jsonObj = new JSONObject(errorMsg);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMsg);
		}
		bidderRep.saveAndFlush(bidderBean);
		logger.info("ending saveBidder");
		return new ResponseEntity<ProjectBean>(bidderBean, HttpStatus.OK);
	}

	@GetMapping("/getAllBidder")
	ResponseEntity<List<ProjectBean>> getAllBidders(
			@RequestHeader("userAgent") String userAgent) {

		if (userAgent != null && !userAgent.isEmpty())
			return new ResponseEntity<List<ProjectBean>>(
					HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<List<ProjectBean>>(bidderRep.findAll(),
				HttpStatus.OK);
	}
	
	@GetMapping("/error")
	 ResponseEntity<ProjectBean> errorBidder() {
		return new ResponseEntity("Not a valid user",
				HttpStatus.UNAUTHORIZED);
		//return new ResponseEntity<List<BidderBean>>(bidderRep.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/getBidder/{BidderId}")
	ResponseEntity<ProjectBean> getBidder(
			@PathVariable("BidderId") Integer bidderId) {
		logger.info("Started getBidder");
		if (bidderId != null && bidderId != 0) {
		logger.info("Ended getBidder");
			return new ResponseEntity<ProjectBean>(
					(ProjectBean) bidderRep.findOne(bidderId),
					HttpStatus.OK);
		} else {
			return new ResponseEntity("bidderId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}

	@GetMapping("/deleteBidder/{BidderId}")
	boolean delete(@PathVariable("BidderId") Integer BidderId) {
		logger.info("Started delete");
		bidderRep.delete(BidderId);
		logger.info("Ended delete");
		return bidderRep.exists(BidderId);
	}

	@GetMapping("/getPageBidder/{start}/{size}")
	ResponseEntity<List<ProjectBean>> getPageBidder(
			@PathVariable("start") Integer start,
			@PathVariable("size") Integer size) {
		logger.info("Started getPageBidder");

		if (start != null && size != null && size != 0) {
			Iterable<ProjectBean> bean = bidderRep.findAll(new PageRequest(
					start, size));
			ArrayList<ProjectBean> bidderList = new ArrayList<ProjectBean>();
			if (bean != null) {
				for (ProjectBean bidderBean : bean) {
					bidderList.add(bidderBean);
				}
		logger.info("Ended getPageBidder");
				return new ResponseEntity<List<ProjectBean>>(bidderList,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<ProjectBean>>(bidderList,
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity("bidderId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}
	
	@GetMapping("/getPageBidder/{start}/{size}/{field}/{dir}")
	ResponseEntity<List<ProjectBean>> getSortPageBidder(
			@PathVariable("start") Integer start,
			@PathVariable("size") Integer size,
			@PathVariable("dir") String direction,
			@PathVariable("field") String field) throws CustomException {
		logger.info("Started getPageBidder");

		if (start != null && size != null && size != 0 && !direction.isEmpty() && direction != null && !field.isEmpty() && field != null) {
			Direction sort = null;
			if(direction.equalsIgnoreCase("ASC"))
				 sort = Sort.Direction.ASC;
			else if(direction.equalsIgnoreCase("DESC")){
				 sort = Sort.Direction.DESC;
			    }
			Iterable<ProjectBean> bean = bidderRep.findAll(new PageRequest(
					start, size,sort,field));
			ArrayList<ProjectBean> bidderList = new ArrayList<ProjectBean>();
			if (bean != null) {
				for (ProjectBean bidderBean : bean) {
					bidderList.add(bidderBean);
				}
		logger.info("Ended getPageBidder");
				return new ResponseEntity<List<ProjectBean>>(bidderList,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<ProjectBean>>(bidderList,
						HttpStatus.OK);
			}
		} else {
			  ExceptionThrower eT= new ExceptionThrower();
			  //eT.throwGeneralException();
			  eT.throwCustomException();
			  //eT.throwNullPointerException();
			return new ResponseEntity("bidderId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}
}
