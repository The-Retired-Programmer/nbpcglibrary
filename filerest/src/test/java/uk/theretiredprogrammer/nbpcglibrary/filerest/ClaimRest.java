/*
 * Copyright 2017 Richard Linsdale (richard at theretiredprogrammer.uk).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.filerest;

/**
 * The Claim Rest Persistence Class.
 *
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class ClaimRest extends FileRest<ClaimEntity> {
    
    
    public ClaimRest() {
        super("/Users/richard/Application testing sandpit/JsonRestDB/auth",
                "claim", ClaimEntity.class, (e) -> ClaimRest.copyEntity(e));
    }
    
    public static ClaimEntity copyEntity(ClaimEntity e) {
        ClaimEntity ne = new ClaimEntity();
        ne.setClaimkey(e.getClaimkey());
        ne.setValue(e.getValue());
        ne.setUser(e.getUser());
        ne.setCreatedby(e.getCreatedby());
        ne.setCreatedon(e.getCreatedon());
        ne.setUpdatedby(e.getUpdatedby());
        ne.setUpdatedon(e.getUpdatedon());
        ne.setId(e.getId());
        return ne;
    }
}
