
from datetime import timedelta, datetime

inCutStart = '00:05:05.2'
inCutEnd = '01:13:19.9'


datetime1 = datetime.strptime(f'2025-01-01 {inCutStart}00000', '%Y-%m-%d %H:%M:%S.%f')
datetime2 = datetime.strptime(f'2025-01-01 {inCutEnd}00000', '%Y-%m-%d %H:%M:%S.%f')
delta21 = datetime2 - datetime1
print(delta21.__class__)
print(str(delta21))
print('')

datetime3 = datetime(year=2025, month=4, day=22, hour=11, minute=31, second=23, microsecond=124442)
datetime4 = datetime(year=2025, month=4, day=22, hour=11, minute=33, second=57, microsecond=8512)
delta43 = datetime4 - datetime3
print(delta43.__class__)
print(str(delta43))
print('')

deltaA = timedelta(days=0, hours=0, minutes=22, seconds=11, milliseconds=842)
deltaB = timedelta(days=0, hours=0, minutes=51, seconds=59, milliseconds=842)
deltaBA = deltaB - deltaA
print(deltaBA.__class__)
print(str(deltaBA))
print('')