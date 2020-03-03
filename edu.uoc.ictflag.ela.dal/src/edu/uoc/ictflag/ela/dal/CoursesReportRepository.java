package edu.uoc.ictflag.ela.dal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import edu.uoc.ictflag.ela.model.CoursesReportDataItem;
import edu.uoc.ictflag.ela.model.FilterField;
import edu.uoc.ictflag.institution.dal.ICourseGroupRepository;
import edu.uoc.ictflag.institution.dal.ICourseRepository;
import edu.uoc.ictflag.institution.dal.IProgramRepository;
import edu.uoc.ictflag.institution.dal.ISemesterRepository;
import edu.uoc.ictflag.institution.dal.ISubjectRepository;
import edu.uoc.ictflag.security.dal.IUserRepository;

@Stateless
@Transactional(TxType.REQUIRED)
public class CoursesReportRepository extends ReportRepository<CoursesReportDataItem> implements ICoursesReportRepository
{
	IExercisesReportRepository exercisesReportRepository;
	
	@Inject
	public CoursesReportRepository(IUserRepository userRepository, IToolRepository toolRepository, IProgramRepository programRepository, ISubjectRepository subjectRepository, ICourseRepository courseRepository, ICourseGroupRepository courseGroupRepository, IAssignmentRepository assignmentRepository, IExerciseRepository exerciseRepository, ISemesterRepository semesterRepository, IExercisesReportRepository exercisesReportRepository)
	{
		this.userRepository = userRepository;
		this.toolRepository = toolRepository;
		this.programRepository = programRepository;
		this.subjectRepository = subjectRepository;
		this.courseRepository = courseRepository;
		this.courseGroupRepository = courseGroupRepository;
		this.assignmentRepository = assignmentRepository;
		this.exerciseRepository = exerciseRepository;
		this.semesterRepository = semesterRepository;
		this.exercisesReportRepository = exercisesReportRepository;
	}
	
	@Override
	protected CoursesReportDataItem createItemForRow(Object[] o)
	{
		return new CoursesReportDataItem(o);
	}
	
	@Override
	protected String ensambleQuery(StringBuilder select, StringBuilder from, StringBuilder[] wheres, StringBuilder groupBy, StringBuilder orderBy,
			StringBuilder limit)
	{
		return "SELECT min(dw.grade) as minNumericGrade, avg(dw.grade) as avgNumericGrade, max(dw.grade) as maxNumericGrade, "
				+ " sum(case when dw.grade >= 8.5 then 1 else 0 end) as A, "
				+ " sum(case when dw.grade >= 6.5 and dw.grade < 8.5 then 1 else 0 end) as B, "
				+ " sum(case when dw.grade >= 5 and dw.grade < 6.5 then 1 else 0 end) as Cplus, "
				+ " sum(case when dw.grade >= 3 and dw.grade < 5 then 1 else 0 end) as Cminus, "
				+ " sum(case when dw.grade < 3 then 1 else 0 end) as D, "
				
				+ " sum(edw.right) as right, sum(edw.wrong) as wrong, sum(edw.timeout) as timeout, sum(edw.error) as error, "
				
				+ "min(dw.duration_sec) / 60.0 as minDuration, " // convert into minutes
				+ "avg(dw.duration_sec) filter (where dw.duration_sec > 0) / 60.0 " // convert into minutes
				+ "as avgDuration, max(dw.duration_sec) / 60.0 as maxDuration " // convert into minutes
				+ select
				+ " FROM AssignmentsDW dw LEFT OUTER JOIN (SELECT e.assignment_id, e.student_id, sum(case when outcome = 'R' then 1 else 0 end) as right, "
				+ " sum(case when outcome = 'W' then 1 else 0 end) as wrong, sum(case when outcome = 'T' then 1 else 0 end) as timeout, "
				+ " sum(case when outcome = 'E' then 1 else 0 end) as error	FROM ExercisesDW e " + wheres[1]
				+ " GROUP BY e.assignment_id, e.student_id) as edw "
				+ " ON dw.assignment_id = edw.assignment_id and dw.student_id = edw.student_id "
				+ from + wheres[0] + groupBy + orderBy + limit + ";";
	}
	
	@Override
	protected String getWhereTableAlias(int whereIndex)
	{
		if (whereIndex == 1)
		{
			return "e";
		}
		
		return super.getWhereTableAlias(whereIndex);
	}
	
	@Override
	protected int[] getWheresToAddFilter(String filter)
	{
		if (filter.equalsIgnoreCase(FilterField.ATTEMPTS))
		{
			return new int[] { 1 };
		}
		
		return new int[] { 0 };
	}
	
	@Override
	protected int getNumberOfWheres()
	{
		return 2;
	}
}
