/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.mllib.classification

import scala.math.abs
import scala.util.Random
import scala.collection.JavaConversions._

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

import org.apache.spark.SparkContext
import org.apache.spark.mllib.regression._
import org.apache.spark.mllib.util.LocalSparkContext

class BinaryClassificationEvaluationSuite extends FunSuite with LocalSparkContext 
    with ShouldMatchers {
  def validateResult(estVal: Double, trueVal: Double, tol: Double) {
    abs(estVal - trueVal) should be < tol
  }

  // Test ROC area under the curve using synthetic output of a model
  test("ROC area under curve, synthetic, LR") {
    val predictionAndLabelC = sc.parallelize(Array((3.0, 1.0), (-2.0, 0.0), (2.0, 1.0), (-1.0, 0.0),
      (1.0, 1.0)))
    val modelC = new LogisticRegressionModel(Array(0.0), 0.0)
    val aucRocC = modelC.areaUnderROC(predictionAndLabelC)
    validateResult(aucRocC, 1.0, 0.01)

    val predictionAndLabelR = sc.parallelize(Array((0.45, 1.0), (-0.23, 0.0), (-0.34, 1.0), 
      (-0.42, 0.0), (0.62, 1.0)))
    val modelR = new LogisticRegressionModel(Array(0.0), 0.0)
    val aucRocR = modelR.areaUnderROC(predictionAndLabelR)
    validateResult(aucRocR, 0.8333, 0.01)
  }

  // Test ROC area under the curve using a small data set and svm
  test("ROC area under curve, sythentic, SVM") {
    val predictionAndLabelC = sc.parallelize(Array((3.0, 1.0), (-2.0, 0.0), (2.0, 1.0), (-1.0, 0.0),
      (1.0, 1.0)))
    val modelC = new SVMModel(Array(0.0), 0.0)
    val aucRocC = modelC.areaUnderROC(predictionAndLabelC)
    validateResult(aucRocC, 1.0, 0.01)

    val predictionAndLabelR = sc.parallelize(Array((0.45, 1.0), (-0.23, 0.0), (-0.34, 1.0), 
      (-0.42, 0.0), (0.62, 1.0)))
    val modelR = new SVMModel(Array(0.0), 0.0)
    val aucRocR = modelR.areaUnderROC(predictionAndLabelR)
    validateResult(aucRocR, 0.8333, 0.01)
  }
}
