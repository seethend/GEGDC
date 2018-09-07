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
@RequestMapping("/project/v1")
public class ProjectControler {

	@Autowired
	ProjectRep projectRep;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@SuppressWarnings("rawtypes")
	@PostMapping("/saveproject")
	ResponseEntity saveProject(@RequestBody ProjectBean projectBean) {
		logger.info("staring saveproject");
		String errorMsg = "";
		
		Set<ConstraintViolation<ProjectBean>> constraintViolations = validator.validate(projectBean);
		if (constraintViolations.size() > 0) {
			for (ConstraintViolation<ProjectBean> cv : constraintViolations) {
				errorMsg = errorMsg + cv.getMessage();
			}
			errorMsg = errorMsg + "$$";
			JSONObject jsonObj = new JSONObject(errorMsg);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(errorMsg);
		}
		projectRep.saveAndFlush(projectBean);
		logger.info("ending saveProject");
		return new ResponseEntity<ProjectBean>(projectBean, HttpStatus.OK);
	}

	@GetMapping("/getallprojects")
	ResponseEntity<List<ProjectBean>> getAllprojects(
			@RequestHeader("userAgent") String userAgent) {

		if (userAgent != null && !userAgent.isEmpty())
			return new ResponseEntity<List<ProjectBean>>(
					HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<List<ProjectBean>>(projectRep.findAll(),
				HttpStatus.OK);
	}
	
	@GetMapping("/error")
	 ResponseEntity<ProjectBean> errorproject() {
		return new ResponseEntity("Not a valid project",
				HttpStatus.UNAUTHORIZED);
		//return new ResponseEntity<List<ProjectBean>>(projectRep.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/getproject/{projectId}")
	ResponseEntity<ProjectBean> getProject(
			@PathVariable("projectId") Integer projectId) {
		logger.info("Started getProject");
		if (projectId != null && projectId != 0) {
		logger.info("Ended getProject");
			return new ResponseEntity<ProjectBean>(
					(ProjectBean) projectRep.findOne(projectId),
					HttpStatus.OK);
		} else {
			return new ResponseEntity("projectId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}

	@GetMapping("/deleteProject/{ProjectId}")
	boolean delete(@PathVariable("ProjectId") Integer ProjectId) {
		logger.info("Started delete");
		projectRep.delete(ProjectId);
		logger.info("Ended delete");
		return projectRep.exists(ProjectId);
	}

	@GetMapping("/getPageProject/{start}/{size}")
	ResponseEntity<List<ProjectBean>> getPageProject(
			@PathVariable("start") Integer start,
			@PathVariable("size") Integer size) {
		logger.info("Started getPageProject");

		if (start != null && size != null && size != 0) {
			Iterable<ProjectBean> bean = projectRep.findAll(new PageRequest(
					start, size));
			ArrayList<ProjectBean> projectList = new ArrayList<ProjectBean>();
			if (bean != null) {
				for (ProjectBean projectBean : bean) {
					projectList.add(projectBean);
				}
		logger.info("Ended getPageProject");
				return new ResponseEntity<List<ProjectBean>>(projectList,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<ProjectBean>>(projectList,
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity("projectId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}
	
	@GetMapping("/getPageProject/{start}/{size}/{field}/{dir}")
	ResponseEntity<List<ProjectBean>> getSortPageProject(
			@PathVariable("start") Integer start,
			@PathVariable("size") Integer size,
			@PathVariable("dir") String direction,
			@PathVariable("field") String field) throws CustomException {
		logger.info("Started getPageProject");

		if (start != null && size != null && size != 0 && !direction.isEmpty() && direction != null && !field.isEmpty() && field != null) {
			Direction sort = null;
			if(direction.equalsIgnoreCase("ASC"))
				 sort = Sort.Direction.ASC;
			else if(direction.equalsIgnoreCase("DESC")){
				 sort = Sort.Direction.DESC;
			    }
			Iterable<ProjectBean> bean = projectRep.findAll(new PageRequest(
					start, size,sort,field));
			ArrayList<ProjectBean> projectList = new ArrayList<ProjectBean>();
			if (bean != null) {
				for (ProjectBean projectBean : bean) {
					projectList.add(projectBean);
				}
		logger.info("Ended getPageProject");
				return new ResponseEntity<List<ProjectBean>>(projectList,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<List<ProjectBean>>(projectList,
						HttpStatus.OK);
			}
		} else {
			  ExceptionThrower eT= new ExceptionThrower();
			  //eT.throwGeneralException();
			  eT.throwCustomException();
			  //eT.throwNullPointerException();
			return new ResponseEntity("projectId is mandatry..",
					HttpStatus.PRECONDITION_REQUIRED);
		}
	}
}
