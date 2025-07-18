
--
-- flowable-engine-7.1.0.jar!\org\flowable\db\create\flowable.postgres.create.engine.sql
--

create table defendev_bpm.ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    CATEGORY_ varchar(255),
    KEY_ varchar(255),
    TENANT_ID_ varchar(255) default '',
    DEPLOY_TIME_ timestamp,
    DERIVED_FROM_ varchar(64),
    DERIVED_FROM_ROOT_ varchar(64),
    PARENT_DEPLOYMENT_ID_ varchar(255),
    ENGINE_VERSION_ varchar(255),
    primary key (ID_)
);

create table defendev_bpm.ACT_RE_MODEL (
    ID_ varchar(64) not null,
    REV_ integer,
    NAME_ varchar(255),
    KEY_ varchar(255),
    CATEGORY_ varchar(255),
    CREATE_TIME_ timestamp,
    LAST_UPDATE_TIME_ timestamp,
    VERSION_ integer,
    META_INFO_ varchar(4000),
    DEPLOYMENT_ID_ varchar(64),
    EDITOR_SOURCE_VALUE_ID_ varchar(64),
    EDITOR_SOURCE_EXTRA_VALUE_ID_ varchar(64),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
);

create table defendev_bpm.ACT_RU_EXECUTION (
    ID_ varchar(64),
    REV_ integer,
    PROC_INST_ID_ varchar(64),
    BUSINESS_KEY_ varchar(255),
    PARENT_ID_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    SUPER_EXEC_ varchar(64),
    ROOT_PROC_INST_ID_ varchar(64),
    ACT_ID_ varchar(255),
    IS_ACTIVE_ boolean,
    IS_CONCURRENT_ boolean,
    IS_SCOPE_ boolean,
    IS_EVENT_SCOPE_ boolean,
    IS_MI_ROOT_ boolean,
    SUSPENSION_STATE_ integer,
    CACHED_ENT_STATE_ integer,
    TENANT_ID_ varchar(255) default '',
    NAME_ varchar(255),
    START_ACT_ID_ varchar(255),
    START_TIME_ timestamp,
    START_USER_ID_ varchar(255),
    LOCK_TIME_ timestamp,
    LOCK_OWNER_ varchar(255),
    IS_COUNT_ENABLED_ boolean,
    EVT_SUBSCR_COUNT_ integer, 
    TASK_COUNT_ integer, 
    JOB_COUNT_ integer, 
    TIMER_JOB_COUNT_ integer,
    SUSP_JOB_COUNT_ integer,
    DEADLETTER_JOB_COUNT_ integer,
    EXTERNAL_WORKER_JOB_COUNT_ integer,
    VAR_COUNT_ integer, 
    ID_LINK_COUNT_ integer,
    CALLBACK_ID_ varchar(255),
    CALLBACK_TYPE_ varchar(255),
    REFERENCE_ID_ varchar(255),
    REFERENCE_TYPE_ varchar(255),
    PROPAGATED_STAGE_INST_ID_ varchar(255),
    BUSINESS_STATUS_ varchar(255),
    primary key (ID_)
);

create table defendev_bpm.ACT_RE_PROCDEF (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255) NOT NULL,
    VERSION_ integer NOT NULL,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    DESCRIPTION_ varchar(4000),
    HAS_START_FORM_KEY_ boolean,
    HAS_GRAPHICAL_NOTATION_ boolean,
    SUSPENSION_STATE_ integer,
    TENANT_ID_ varchar(255) default '',
    DERIVED_FROM_ varchar(64),
    DERIVED_FROM_ROOT_ varchar(64),
    DERIVED_VERSION_ integer NOT NULL default 0,
    ENGINE_VERSION_ varchar(255),
    primary key (ID_)
);

create table defendev_bpm.ACT_EVT_LOG (
    LOG_NR_ SERIAL PRIMARY KEY,
    TYPE_ varchar(64),
    PROC_DEF_ID_ varchar(64),
    PROC_INST_ID_ varchar(64),
    EXECUTION_ID_ varchar(64),
    TASK_ID_ varchar(64),
    TIME_STAMP_ timestamp not null,
    USER_ID_ varchar(255),
    DATA_ bytea,
    LOCK_OWNER_ varchar(255),
    LOCK_TIME_ timestamp null,
    IS_PROCESSED_ smallint default 0
);

create table defendev_bpm.ACT_PROCDEF_INFO (
	ID_ varchar(64) not null,
    PROC_DEF_ID_ varchar(64) not null,
    REV_ integer,
    INFO_JSON_ID_ varchar(64),
    primary key (ID_)
);

create table defendev_bpm.ACT_RU_ACTINST (
    ID_ varchar(64) not null,
    REV_ integer default 1,
    PROC_DEF_ID_ varchar(64) not null,
    PROC_INST_ID_ varchar(64) not null,
    EXECUTION_ID_ varchar(64) not null,
    ACT_ID_ varchar(255) not null,
    TASK_ID_ varchar(64),
    CALL_PROC_INST_ID_ varchar(64),
    ACT_NAME_ varchar(255),
    ACT_TYPE_ varchar(255) not null,
    ASSIGNEE_ varchar(255),
    START_TIME_ timestamp not null,
    END_TIME_ timestamp,
    DURATION_ bigint,
    TRANSACTION_ORDER_ integer,
    DELETE_REASON_ varchar(4000),
    TENANT_ID_ varchar(255) default '',
    primary key (ID_)
);

create index ACT_IDX_EXEC_BUSKEY on defendev_bpm.ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDX_EXE_ROOT on defendev_bpm.ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);
create index ACT_IDX_EXEC_REF_ID_ on defendev_bpm.ACT_RU_EXECUTION(REFERENCE_ID_);
create index ACT_IDX_VARIABLE_TASK_ID on defendev_bpm.ACT_RU_VARIABLE(TASK_ID_);

create index ACT_IDX_BYTEAR_DEPL on defendev_bpm.ACT_GE_BYTEARRAY(DEPLOYMENT_ID_);

create index ACT_IDX_RU_ACTI_START on defendev_bpm.ACT_RU_ACTINST(START_TIME_);
create index ACT_IDX_RU_ACTI_END on defendev_bpm.ACT_RU_ACTINST(END_TIME_);
create index ACT_IDX_RU_ACTI_PROC on defendev_bpm.ACT_RU_ACTINST(PROC_INST_ID_);
create index ACT_IDX_RU_ACTI_PROC_ACT on defendev_bpm.ACT_RU_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_RU_ACTI_EXEC on defendev_bpm.ACT_RU_ACTINST(EXECUTION_ID_);
create index ACT_IDX_RU_ACTI_EXEC_ACT on defendev_bpm.ACT_RU_ACTINST(EXECUTION_ID_, ACT_ID_);
create index ACT_IDX_RU_ACTI_TASK on defendev_bpm.ACT_RU_ACTINST(TASK_ID_);

alter table defendev_bpm.ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
    foreign key (DEPLOYMENT_ID_) 
    references defendev_bpm.ACT_RE_DEPLOYMENT (ID_);

alter table defendev_bpm.ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
    unique (KEY_,VERSION_, DERIVED_VERSION_, TENANT_ID_);
    
create index ACT_IDX_EXE_PROCINST on defendev_bpm.ACT_RU_EXECUTION(PROC_INST_ID_);
alter table defendev_bpm.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST 
    foreign key (PROC_INST_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_EXE_PARENT on defendev_bpm.ACT_RU_EXECUTION(PARENT_ID_);
alter table defendev_bpm.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
    foreign key (PARENT_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_EXE_SUPER on defendev_bpm.ACT_RU_EXECUTION(SUPER_EXEC_);
alter table defendev_bpm.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
    foreign key (SUPER_EXEC_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    

create index ACT_IDX_EXE_PROCDEF on defendev_bpm.ACT_RU_EXECUTION(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF 
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);
    

create index ACT_IDX_TSKASS_TASK on defendev_bpm.ACT_RU_IDENTITYLINK(TASK_ID_);
alter table defendev_bpm.ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
    foreign key (TASK_ID_) 
    references defendev_bpm.ACT_RU_TASK (ID_);
    
create index ACT_IDX_ATHRZ_PROCEDEF on defendev_bpm.ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);
    
create index ACT_IDX_IDL_PROCINST on defendev_bpm.ACT_RU_IDENTITYLINK(PROC_INST_ID_);
alter table defendev_bpm.ACT_RU_IDENTITYLINK
    add constraint ACT_FK_IDL_PROCINST
    foreign key (PROC_INST_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_TASK_EXEC on defendev_bpm.ACT_RU_TASK(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
    foreign key (EXECUTION_ID_)
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_TASK_PROCINST on defendev_bpm.ACT_RU_TASK(PROC_INST_ID_);
alter table defendev_bpm.ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
    foreign key (PROC_INST_ID_)
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_TASK_PROCDEF on defendev_bpm.ACT_RU_TASK(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_TASK
  add constraint ACT_FK_TASK_PROCDEF
  foreign key (PROC_DEF_ID_)
  references defendev_bpm.ACT_RE_PROCDEF (ID_);
  
create index ACT_IDX_VAR_EXE on defendev_bpm.ACT_RU_VARIABLE(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
    foreign key (EXECUTION_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_VAR_PROCINST on defendev_bpm.ACT_RU_VARIABLE(PROC_INST_ID_);
alter table defendev_bpm.ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
    foreign key (PROC_INST_ID_)
    references defendev_bpm.ACT_RU_EXECUTION(ID_);

create index ACT_IDX_JOB_EXECUTION_ID on defendev_bpm.ACT_RU_JOB(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_JOB
    add constraint ACT_FK_JOB_EXECUTION 
    foreign key (EXECUTION_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_JOB_PROCESS_INSTANCE_ID on defendev_bpm.ACT_RU_JOB(PROCESS_INSTANCE_ID_);
alter table defendev_bpm.ACT_RU_JOB
    add constraint ACT_FK_JOB_PROCESS_INSTANCE 
    foreign key (PROCESS_INSTANCE_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_JOB_PROC_DEF_ID on defendev_bpm.ACT_RU_JOB(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_JOB
    add constraint ACT_FK_JOB_PROC_DEF
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);

create index ACT_IDX_TIMER_JOB_EXECUTION_ID on defendev_bpm.ACT_RU_TIMER_JOB(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_EXECUTION 
    foreign key (EXECUTION_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TIMER_JOB_PROCESS_INSTANCE_ID on defendev_bpm.ACT_RU_TIMER_JOB(PROCESS_INSTANCE_ID_);
alter table defendev_bpm.ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_PROCESS_INSTANCE 
    foreign key (PROCESS_INSTANCE_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_TIMER_JOB_PROC_DEF_ID on defendev_bpm.ACT_RU_TIMER_JOB(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_PROC_DEF
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);

create index ACT_IDX_SUSPENDED_JOB_EXECUTION_ID on defendev_bpm.ACT_RU_SUSPENDED_JOB(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_EXECUTION 
    foreign key (EXECUTION_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_SUSPENDED_JOB_PROCESS_INSTANCE_ID on defendev_bpm.ACT_RU_SUSPENDED_JOB(PROCESS_INSTANCE_ID_);
alter table defendev_bpm.ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE 
    foreign key (PROCESS_INSTANCE_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);

create index ACT_IDX_SUSPENDED_JOB_PROC_DEF_ID on defendev_bpm.ACT_RU_SUSPENDED_JOB(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_PROC_DEF
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);

create index ACT_IDX_DEADLETTER_JOB_EXECUTION_ID on defendev_bpm.ACT_RU_DEADLETTER_JOB(EXECUTION_ID_);
alter table defendev_bpm.ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_EXECUTION 
    foreign key (EXECUTION_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
 
create index ACT_IDX_DEADLETTER_JOB_PROCESS_INSTANCE_ID on defendev_bpm.ACT_RU_DEADLETTER_JOB(PROCESS_INSTANCE_ID_);
alter table defendev_bpm.ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE 
    foreign key (PROCESS_INSTANCE_ID_) 
    references defendev_bpm.ACT_RU_EXECUTION (ID_);
    
create index ACT_IDX_DEADLETTER_JOB_PROC_DEF_ID on defendev_bpm.ACT_RU_DEADLETTER_JOB(PROC_DEF_ID_);
alter table defendev_bpm.ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_PROC_DEF
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);
    
alter table defendev_bpm.ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC
    foreign key (EXECUTION_ID_)
    references defendev_bpm.ACT_RU_EXECUTION(ID_);

create index ACT_IDX_MODEL_SOURCE on defendev_bpm.ACT_RE_MODEL(EDITOR_SOURCE_VALUE_ID_);
alter table defendev_bpm.ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE 
    foreign key (EDITOR_SOURCE_VALUE_ID_) 
    references defendev_bpm.ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_MODEL_SOURCE_EXTRA on defendev_bpm.ACT_RE_MODEL(EDITOR_SOURCE_EXTRA_VALUE_ID_);
alter table defendev_bpm.ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE_EXTRA 
    foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_) 
    references defendev_bpm.ACT_GE_BYTEARRAY (ID_);
    
create index ACT_IDX_MODEL_DEPLOYMENT on defendev_bpm.ACT_RE_MODEL(DEPLOYMENT_ID_);
alter table defendev_bpm.ACT_RE_MODEL
    add constraint ACT_FK_MODEL_DEPLOYMENT 
    foreign key (DEPLOYMENT_ID_) 
    references defendev_bpm.ACT_RE_DEPLOYMENT (ID_);

create index ACT_IDX_PROCDEF_INFO_JSON on defendev_bpm.ACT_PROCDEF_INFO(INFO_JSON_ID_);
alter table defendev_bpm.ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_JSON_BA 
    foreign key (INFO_JSON_ID_) 
    references defendev_bpm.ACT_GE_BYTEARRAY (ID_);

create index ACT_IDX_PROCDEF_INFO_PROC on defendev_bpm.ACT_PROCDEF_INFO(PROC_DEF_ID_);
alter table defendev_bpm.ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_PROCDEF 
    foreign key (PROC_DEF_ID_) 
    references defendev_bpm.ACT_RE_PROCDEF (ID_);
    
alter table defendev_bpm.ACT_PROCDEF_INFO
    add constraint ACT_UNIQ_INFO_PROCDEF
    unique (PROC_DEF_ID_);

insert into defendev_bpm.ACT_GE_PROPERTY
values ('schema.version', '7.1.0.2', 1);

insert into defendev_bpm.ACT_GE_PROPERTY
values ('schema.history', 'create(7.1.0.2)', 1);
