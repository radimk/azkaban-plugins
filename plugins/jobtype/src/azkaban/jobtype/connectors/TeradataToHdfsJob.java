/*
 * Copyright 2012 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.jobtype.connectors;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;

import azkaban.jobtype.*;
import azkaban.utils.Props;

public class TeradataToHdfsJob extends HadoopJavaJob {
  public TeradataToHdfsJob(String jobid,
                           Props sysProps,
                           Props jobProps,
                           Logger log) {
    super(jobid, sysProps, jobProps, log);
    jobProps.put(TdchConstants.LIB_JARS_KEY, sysProps.get(TdchConstants.LIB_JARS_KEY));
    TeraDataWalletInitializer.initialize(new File(getCwd()), new File(sysProps.get(TdchConstants.TD_WALLET_JAR)));
  }

  @Override
  protected String getJavaClass() {
    return TeradataToHdfsJobRunnerMain.class.getName();
  }

  @Override
  protected List<String> getClassPaths() {
    return ImmutableList.<String>builder()
                        //TDCH w. Tdwallet requires a classpath point to unjarred folder.
                        .add(TeraDataWalletInitializer.getTdchUnjarFolder())
                        .add(TeraDataWalletInitializer.getTdchUnjarFolder() + File.separator + "lib" + File.separator + "*")
                        .addAll(super.getClassPaths()).build();
  }
}
