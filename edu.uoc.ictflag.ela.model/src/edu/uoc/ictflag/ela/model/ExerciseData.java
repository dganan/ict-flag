package edu.uoc.ictflag.ela.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.uoc.ictflag.core.model.IIdentifiable;
import edu.uoc.ictflag.institution.model.Course;
import edu.uoc.ictflag.institution.model.CourseGroup;
import edu.uoc.ictflag.institution.model.Program;
import edu.uoc.ictflag.institution.model.Semester;
import edu.uoc.ictflag.institution.model.Subject;
import edu.uoc.ictflag.security.model.User;

@Entity
@Table(name = "ExercisesDW")
public class ExerciseData implements Serializable, IIdentifiable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private Tool tool;
	
	private User student;
	
	private boolean studentFixedUp;
	
	private Program program;
	
	private boolean programFixedUp;
	
	private Subject subject;
	
	private boolean subjectFixedUp;
	
	private Semester semester;
	
	private boolean semesterFixedUp;
	
	private Course course;
	
	private boolean courseFixedUp;
	
	private CourseGroup courseGroup;
	
	private Assignment assignment;
	
	private Exercise exercise;
	
	private Outcome outcome;
	
	private boolean outcomeFixedUp;
	
	private double grade;
	
	private boolean gradeIncorrect;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	private boolean startDateFixedUp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	private boolean endDateFixedUp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date assessmentDate;
	
	private long duration_sec;
	private long timeFromLastAttempt_sec;
	private int attemptNumber;
	private boolean lastAttempt;
	
	public ExerciseData()
	{
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Tool getTool()
	{
		return tool;
	}
	
	public void setTool(Tool tool)
	{
		this.tool = tool;
	}
	
	public User getStudent()
	{
		return student;
	}
	
	public void setStudent(User student)
	{
		this.student = student;
	}
	
	public Program getProgram()
	{
		return program;
	}
	
	public void setProgram(Program program)
	{
		this.program = program;
	}
	
	public Subject getSubject()
	{
		return subject;
	}
	
	public void setSubject(Subject subject)
	{
		this.subject = subject;
	}
	
	public Semester getSemester()
	{
		return semester;
	}
	
	public void setSemester(Semester semester)
	{
		this.semester = semester;
	}
	
	public Course getCourse()
	{
		return course;
	}
	
	public void setCourse(Course course)
	{
		this.course = course;
	}
	
	public CourseGroup getCourseGroup()
	{
		return courseGroup;
	}
	
	public void setCourseGroup(CourseGroup courseGroup)
	{
		this.courseGroup = courseGroup;
	}
	
	public Date getStartDate()
	{
		return startDate;
	}
	
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	
	public Date getEndDate()
	{
		return endDate;
	}
	
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	
	public Date getAssessmentDate()
	{
		return assessmentDate;
	}
	
	public void setAssessmentDate(Date assessmentDate)
	{
		this.assessmentDate = assessmentDate;
	}
	
	public Assignment getAssignment()
	{
		return assignment;
	}
	
	public void setAssignment(Assignment assignment)
	{
		this.assignment = assignment;
	}
	
	public Exercise getExercise()
	{
		return exercise;
	}
	
	public void setExercise(Exercise exercise)
	{
		this.exercise = exercise;
	}
	
	public Outcome getOutcome()
	{
		return outcome;
	}
	
	public void setOutcome(Outcome outcome)
	{
		this.outcome = outcome;
	}
	
	public boolean isGradeNull()
	{
		return grade == -1 && !gradeIncorrect;
	}
	
	public double getGrade()
	{
		return grade;
	}
	
	public void setGrade(double grade)
	{
		this.grade = grade;
	}
	
	public long getDuration_sec()
	{
		return duration_sec;
	}
	
	public void setDuration_sec(long duration_sec)
	{
		this.duration_sec = duration_sec;
	}
	
	public long getTimeFromLastAttempt_sec()
	{
		return timeFromLastAttempt_sec;
	}
	
	public void setTimeFromLastAttempt_sec(long timeFromLastAttempt_sec)
	{
		this.timeFromLastAttempt_sec = timeFromLastAttempt_sec;
	}
	
	public int getAttemptNumber()
	{
		return attemptNumber;
	}
	
	public void setAttemptNumber(int attemptNumber)
	{
		this.attemptNumber = attemptNumber;
	}

	public boolean isLastAttempt()
	{
		return lastAttempt;
	}
	
	public void setLastAttempt(boolean lastAttempt)
	{
		this.lastAttempt = lastAttempt;
	}
	
	public boolean isStudentFixedUp()
	{
		return studentFixedUp;
	}
	
	public void setStudentFixedUp(boolean studentFixedUp)
	{
		this.studentFixedUp = studentFixedUp;
	}

	public boolean isProgramFixedUp()
	{
		return programFixedUp;
	}
	
	public void setProgramFixedUp(boolean programFixedUp)
	{
		this.programFixedUp = programFixedUp;
	}
	
	public boolean isSubjectFixedUp()
	{
		return subjectFixedUp;
	}
	
	public void setSubjectFixedUp(boolean subjectFixedUp)
	{
		this.subjectFixedUp = subjectFixedUp;
	}
	
	public boolean isSemesterFixedUp()
	{
		return semesterFixedUp;
	}
	
	public void setSemesterFixedUp(boolean semesterFixedUp)
	{
		this.semesterFixedUp = semesterFixedUp;
	}
	
	public boolean isCourseFixedUp()
	{
		return courseFixedUp;
	}
	
	public void setCourseFixedUp(boolean courseFixedUp)
	{
		this.courseFixedUp = courseFixedUp;
	}
	
	public boolean isGradeIncorrect()
	{
		return gradeIncorrect;
	}
	
	public void setGradeIncorrect(boolean gradeIncorrect)
	{
		this.gradeIncorrect = gradeIncorrect;
	}
	
	public boolean isOutcomeFixedUp()
	{
		return outcomeFixedUp;
	}
	
	public void setOutcomeFixedUp(boolean outcomeFixedUp)
	{
		this.outcomeFixedUp = outcomeFixedUp;
	}
	
	public boolean isStartDateFixedUp()
	{
		return startDateFixedUp;
	}
	
	public void setStartDateFixedUp(boolean startDateFixedUp)
	{
		this.startDateFixedUp = startDateFixedUp;
	}
	
	public boolean isEndDateFixedUp()
	{
		return endDateFixedUp;
	}
	
	public void setEndDateFixedUp(boolean endDateFixedUp)
	{
		this.endDateFixedUp = endDateFixedUp;
	}
	
	public boolean hasInconsistentOrIncompleteData()
	{
		return isStartDateFixedUp() || endDate == null || isOutcomeFixedUp() || isGradeIncorrect();
	}
}
