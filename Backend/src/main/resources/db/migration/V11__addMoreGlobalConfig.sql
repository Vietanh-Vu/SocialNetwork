USE
socialnetwork;

INSERT INTO socialnetwork.global_configs (name, code, `desc`, created)
VALUES ('ABBREVIATIONS', 'ABBREVIATIONS',
        '{"cc":"con cặc","ccm":"con cặc mẹ","ccvkl":"con cặc vkl","cặc":"cặc","clm":"cặc lồn mẹ","clmvkl":"cặc lồn mẹ vkl","đcm":"địt con mẹ","dcm":"địt con mẹ","đcmn":"địt con mẹ nó","đcmnr":"địt con mẹ nó rồi","đcmr":"địt con mẹ rồi","đcmvkl":"địt con mẹ vkl","đmm":"địt mẹ mày","đml":"địt mẹ lồn","đm":"địt mẹ","đmẹ":"địt mẹ","đmvkl":"địt mẹ vkl","đmvklm":"địt mẹ vkl mày","đmvklmn":"địt mẹ vkl mày nha","đéo":"đéo","đéo m":"đéo mày","đĩ":"đĩ","đĩ m":"đĩ mẹ","đĩ con":"đĩ con","lồn":"lồn","lồn m":"lồn mẹ","lồn cụ":"lồn cụ","lồn cha":"lồn cha","đjt":"địt","đjt m":"địt mẹ","đjt l":"địt lồn","dm":"địt mẹ","dm m":"địt mẹ mày","dmvl":"địt mẹ vãi lồn","dmvlm":"địt mẹ vãi lồn mày","dmvlmn":"địt mẹ vãi lồn mày nha","cmm":"con mẹ mày","cmmn":"con mẹ mày nha","ccmvkl":"con cặc mẹ vkl","vl":"vãi lồn","vkl":"vãi cứt lồn","vklm":"vãi cứt lồn mày","vklmn":"vãi cứt lồn mày nha","vklmnz":"vãi cứt lồn mày nha z","vlm":"vãi lồn mày","vlmn":"vãi lồn mày nha","dmql":"địt mẹ quả lồn","dmqlm":"địt mẹ quả lồn mày","ccql":"con cặc quả lồn","ccqlm":"con cặc quả lồn mày","đjm":"địt mẹ","đjmvl":"địt mẹ vãi lồn","đjmvlm":"địt mẹ vãi lồn mày","ccml":"con cặc mẹ lồn","đmlm":"địt mẹ lồn mày","đmlmn":"địt mẹ lồn mày nha","đcm m":"địt con mẹ mày","đcmvl":"địt con mẹ vãi lồn","ccvl":"con cặc vãi lồn","ccvklm":"con cặc vãi cứt lồn mày","đmm m":"địt mẹ mày","đmmvl":"địt mẹ vãi lồn","đmmvlm":"địt mẹ vãi lồn mày","đmmvlmn":"địt mẹ vãi lồn mày nha","đm m":"địt mẹ mày","đmvl":"địt mẹ vãi lồn","đmvlm":"địt mẹ vãi lồn mày","đmvlmn":"địt mẹ vãi lồn mày nha","đcmvlm":"địt con mẹ vãi lồn mày","đcmvlmn":"địt con mẹ vãi lồn mày nha","đcmvlmzn":"địt con mẹ vãi lồn mày nha z","đmvlmzn":"địt mẹ vãi lồn mày nha z","ccvlm":"con cặc vãi lồn mày","ccvlmn":"con cặc vãi lồn mày nha","ccvlmzn":"con cặc vãi lồn mày nha z"}',
        CURRENT_TIMESTAMP);
INSERT INTO socialnetwork.global_configs (name, code, `desc`, created)
VALUES ('LEET_MAPPING', 'LEET_MAPPING',
        '{"0":"o","1":"i","2":"z","3":"e","4":"a","5":"s","6":"g","7":"t","8":"b","9":"g","@":"a","$":"s","!":"i","+":"t","|3":"b","(":"c","<":"c","{":"c","[":"c","|":"l","£":"l","µ":"u","^":"v","%":"x"}',
        CURRENT_TIMESTAMP);
INSERT INTO socialnetwork.global_configs (name, code, `desc`, created)
VALUES ('HOMOGLYPHS_MAPPING', 'HOMOGLYPHS_MAPPING',
        '{"а":"a","ɑ":"a","ｅ":"e","е":"e","і":"i","о":"o","υ":"u","ß":"ss","ｓ":"s","ѕ":"s","ç":"c","ć":"c","č":"c","ď":"d","ԁ":"d","э":"e","г":"r","т":"t","х":"x","у":"y","ј":"j","ʏ":"y","ʃ":"s","ʒ":"z","œ":"oe","ƒ":"f","ђ":"d","љ":"lj","њ":"nj","ћ":"c","ќ":"k","ѣ":"e","џ":"dz"}',
        CURRENT_TIMESTAMP);

-- Thêm cấu hình số lần spam tối đa
INSERT INTO global_configs (name, code, `desc`, created)
VALUES ('Max Spam Count', 'max_spam_count', '5', CURRENT_TIMESTAMP);

-- Thêm cấu hình thời gian khóa (giờ)
INSERT INTO global_configs (name, code, `desc`, created)
VALUES ('Ban Duration Hours', 'ban_duration_hours', '24', CURRENT_TIMESTAMP);