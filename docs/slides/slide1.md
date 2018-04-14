```scala
trait FraudRuleTemplate extends RuleTemplateI {
  protected[scoring] def matches(fraudHistory: FraudHistory,
                                 ruleParams: Params): Boolean
  final def matches(ruleParams: Params, tx: ViewWithDeviceInfo,
                    scoringRecordHistory: => ScoringRecordHistory,
                    fraudHistory: => FraudHistory): Boolean =
    matches(fraudHistory, ruleParams)
}

```