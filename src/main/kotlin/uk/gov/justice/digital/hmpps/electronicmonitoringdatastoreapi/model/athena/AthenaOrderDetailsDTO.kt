package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import com.fasterxml.jackson.annotation.JsonProperty

// TODO: Check which fields are nullable
data class AthenaOrderDetailsDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val firstName: String? = "",
  val lastName: String? = "",
  val alias: String? = "",
  val dateOfBirth: String? = "",
  val adultOrChild: String? = "",
  val sex: String? = "",
  val contact: String? = "",
  @JsonProperty("primary_address_line_1")
  val address1: String? = "",
  @JsonProperty("primary_address_line_2")
  val address2: String? = "",
  @JsonProperty("primary_address_line_3")
  val address3: String? = "",
  val primaryAddressPostCode: String? = "",
  val phoneOrMobileNumber: String? = "",
  val ppo: String? = "",
  val mappa: String? = "",
  val technicalBail: String? = "",
  val manualRisk: String? = "",
  val offenceRisk: Boolean, //  TODO: Check type
  val postCodeRisk: String? = "",
  val falseLimbRisk: String? = "",
  val migratedRisk: String? = "",
  val rangeRisk: String? = "",
  val reportRisk: String? = "",
  val orderStartDate: String? = "",
  val orderEndDate: String? = "",
  val orderType: String? = "",
  val orderTypeDescription: String? = "",
  val orderTypeDetail: String? = "",
  val wearingWristPid: String? = "",
  val notifyingOrganisationDetailsName: String? = "",
  val responsibleOrganisation: String? = "",
  val responsibleOrganisationDetailsRegion: String? = "",
)
//
// ResultSet(Rows=[
//
// Row(Data=[Datum(VarCharValue=legacy_subject_id), Datum(VarCharValue=legacy_order_id), Datum(VarCharValue=first_name), Datum(VarCharValue=last_name), Datum(VarCharValue=alias), Datum(VarCharValue=date_of_birth), Datum(VarCharValue=adult_or_child), Datum(VarCharValue=sex), Datum(VarCharValue=contact), Datum(VarCharValue=primary_address_line_1), Datum(VarCharValue=primary_address_line_2), Datum(VarCharValue=primary_address_line_3), Datum(VarCharValue=primary_address_post_code), Datum(VarCharValue=phone_or_mobile_number), Datum(VarCharValue=ppo), Datum(VarCharValue=mappa), Datum(VarCharValue=technical_bail), Datum(VarCharValue=manual_risk), Datum(VarCharValue=offence_risk), Datum(VarCharValue=post_code_risk), Datum(VarCharValue=false_limb_risk), Datum(VarCharValue=migrated_risk), Datum(VarCharValue=range_risk), Datum(VarCharValue=report_risk), Datum(VarCharValue=order_start_date), Datum(VarCharValue=order_end_date), Datum(VarCharValue=order_type), Datum(VarCharValue=order_type_description), Datum(VarCharValue=order_type_detail), Datum(VarCharValue=wearing_wrist_pid), Datum(VarCharValue=notifying_organisation_details_name), Datum(VarCharValue=responsible_organisation), Datum(VarCharValue=responsible_organisation_details_region)])

// Row(Data=[Datum(VarCharValue=1253587), Datum(VarCharValue=1250042), Datum(VarCharValue=ELLEN), Datum(VarCharValue=RIPLY), Datum(), Datum(VarCharValue=1949-10-08), Datum(VarCharValue=Adult), Datum(VarCharValue=Female), Datum(), Datum(VarCharValue=310 Lightbowne Road, Moston), Datum(VarCharValue=Moston), Datum(VarCharValue=Manchester), Datum(VarCharValue=M40 0FJ), Datum(VarCharValue=07971234567), Datum(VarCharValue=No), Datum(VarCharValue=No Mappa), Datum(VarCharValue=No), Datum(), Datum(), Datum(), Datum(), Datum(), Datum(), Datum(), Datum(VarCharValue=2019-10-24), Datum(VarCharValue=2020-03-24), Datum(VarCharValue=Community), Datum(VarCharValue=CJA03 Suspended Sentence), Datum(VarCharValue=Single Requirement Curfew), Datum(VarCharValue=No), Datum(VarCharValue=Liverpool Magistrates' Court), Datum(), Datum(VarCharValue=No Organisation Identified)])],
//
//
// ResultSetMetadata=ResultSetMetadata(
// ColumnInfo=[
// ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=legacy_subject_id, Label=legacy_subject_id, Type=bigint, Precision=19, Scale=0, Nullable=UNKNOWN, CaseSensitive=false), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=legacy_order_id, Label=legacy_order_id, Type=bigint, Precision=19, Scale=0, Nullable=UNKNOWN, CaseSensitive=false), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=first_name, Label=first_name, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=last_name, Label=last_name, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=alias, Label=alias, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=date_of_birth, Label=date_of_birth, Type=date, Precision=0, Scale=0, Nullable=UNKNOWN, CaseSensitive=false), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=adult_or_child, Label=adult_or_child, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=sex, Label=sex, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=contact, Label=contact, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=primary_address_line_1, Label=primary_address_line_1, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=primary_address_line_2, Label=primary_address_line_2, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=primary_address_line_3, Label=primary_address_line_3, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=primary_address_post_code, Label=primary_address_post_code, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=phone_or_mobile_number, Label=phone_or_mobile_number, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=ppo, Label=ppo, Type=varchar, Precision=3, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=mappa, Label=mappa, Type=varchar, Precision=8, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=technical_bail, Label=technical_bail, Type=varchar, Precision=3, Scale=0, Nullable=UNKNOWN, CaseSensitive=true),
//
// ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=manual_risk, Label=manual_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true),
//
// ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=offence_risk, Label=offence_risk, Type=boolean, Precision=0, Scale=0, Nullable=UNKNOWN, CaseSensitive=false),
//
//
// ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=post_code_risk, Label=post_code_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=false_limb_risk, Label=false_limb_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=migrated_risk, Label=migrated_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=range_risk, Label=range_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=report_risk, Label=report_risk, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=order_start_date, Label=order_start_date, Type=date, Precision=0, Scale=0, Nullable=UNKNOWN, CaseSensitive=false), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=order_end_date, Label=order_end_date, Type=date, Precision=0, Scale=0, Nullable=UNKNOWN, CaseSensitive=false), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=order_type, Label=order_type, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=order_type_description, Label=order_type_description, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=order_type_detail, Label=order_type_detail, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=wearing_wrist_pid, Label=wearing_wrist_pid, Type=varchar, Precision=3, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=notifying_organisation_details_name, Label=notifying_organisation_details_name, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=responsible_organisation, Label=responsible_organisation, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true), ColumnInfo(CatalogName=hive, SchemaName=, TableName=, Name=responsible_organisation_details_region, Label=responsible_organisation_details_region, Type=varchar, Precision=2147483647, Scale=0, Nullable=UNKNOWN, CaseSensitive=true)]))
